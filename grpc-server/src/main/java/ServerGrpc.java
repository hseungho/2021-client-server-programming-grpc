import applicationservice.CourseService;
import applicationservice.StudentService;
import com.google.protobuf.Empty;
import entity.Course;
import entity.Student;
import exception.MyException;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.netty.NettyServerBuilder;
import vo.CourseVO;
import vo.StudentVO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class ServerGrpc implements GrpcInterface {

    public static ServerGrpc instance;
    public static ServerGrpc getInstance() {
        return instance != null ? instance : new ServerGrpc();
    }

    private Server server;
    private ManagedChannel channel;
    private ClientServerProtoGrpc.ClientServerProtoBlockingStub stub;
    private StudentService studentService;
    private CourseService courseService;

    public ServerGrpc() {
        init();
    }

    private void init() {
        initChannel();
        initStub();
        studentService = new StudentService();
    }

    private void initChannel() {
        channel = ManagedChannelBuilder
                .forAddress("localhost", 9090)
                .usePlaintext()
                .build();
    }

    private void initStub() {
        stub = ClientServerProtoGrpc.newBlockingStub(channel);
    }

    public void start() {
        System.out.println("<<<<< SERVER START >>>>>");
        this.startServer();
    }

    @Override
    public ClientServer.StudentList getAllStudentList() {
        System.out.println("LOG: getAllStudentList");
        List<Student> students = studentService.getAllStudentList();
        return StudentDtoConverter.toProtoStudentList(students);
    }

    @Override
    public ClientServer.CourseList getAllCourseList() {
        System.out.println("CALLED METHOD: getAllCourseList");
        List<Course> courses = courseService.getAllCourseList();
        return CourseDtoConverter.toProtoCourseList(courses);
    }

    @Override
    public ClientServer.Status addStudent(ClientServer.Student studentDto) {
        System.out.println("CALLED METHOD: addStudent");
        StudentVO studentVo = StudentDtoConverter.toVO(studentDto);
        try {
            studentService.addStudent(studentVo);
        } catch (MyException.DuplicationDataException e) {
            System.err.printf("LOG: create duplicate student, request id: %s", studentVo.getStudentId());
            return ClientServer.Status.newBuilder()
                    .setStatus(409)
                    .setMessage(e.getMessage())
                    .build();
        }
        return ClientServer.Status.newBuilder().setStatus(201).build();
    }

    @Override
    public ClientServer.Status addCourse(ClientServer.Course courseDto) {
        System.out.println("CALLED METHOD: addCourse");
        CourseVO courseVO = CourseDtoConverter.toVO(courseDto);
        try {
            courseService.addCourse(courseVO);
        } catch (MyException.DuplicationDataException e) {
            System.err.printf("LOG: create duplicate course, request id: %s", courseVO.getCourseId());
            return ClientServer.Status.newBuilder()
                    .setStatus(409)
                    .setMessage(e.getMessage())
                    .build();
        }
        return ClientServer.Status.newBuilder().setStatus(201).build();
    }

    @Override
    public ClientServer.IdList getStudentIdList() {
        System.out.println("CALLED METHOD: getStudentIdList");
        return stub.getStudentIdList(Empty.newBuilder().build());
    }

    @Override
    public ClientServer.IdList getCourseIdList() {
        System.out.println("CALLED METHOD: getCourseIdList");
        return stub.getCourseIdList(Empty.newBuilder().build());
    }

    @Override
    public ClientServer.Status deleteStudent(ClientServer.Id studentId) {
        System.out.println("CALLED METHOD: deleteStudent [ID: "+studentId.getId()+"]");
        if(!isExistStudentId(studentId.getId())) {
            return ClientServer.Status.newBuilder()
                    .setStatus(409)
                    .setMessage("This student id is not exist.").build();
        }
        return stub.deleteStudent(studentId);
    }

    @Override
    public ClientServer.Status deleteCourse(ClientServer.Id courseId) {
        System.out.println("CALLED METHOD: deleteCourse [ID: "+courseId.getId()+"]");
        if(!isExistCourseId(courseId.getId())) {
            return ClientServer.Status.newBuilder()
                    .setStatus(409)
                    .setMessage("This course id is not exist.").build();
        }
        return stub.deleteCourse(courseId);
    }

    /** 학생 ID 유효성 검증 */
    private boolean isExistStudentId(String id) {
        ClientServer.IdList studentIdList = getStudentIdList();
        List<String> studentIds = new ArrayList<>(studentIdList.getIdList());
        return studentIds.contains(id);
    }

    private boolean isExistCourseId(String id) {
        ClientServer.IdList courseIdList = getCourseIdList();
        List<String> courseIds = new ArrayList<>(courseIdList.getIdList());
        return courseIds.contains(id);
    }

    private void startServer() {
        Thread serverThread = new Thread(() -> {
            int port = 8080;
            server = NettyServerBuilder
                    .forAddress(new InetSocketAddress("localhost", port))
                    .addService(new ServerClientServerImpl())
                    .build();

            try {
                server.start();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.err.println("\nServer: gRPC 서버를 종료합니다.");
                System.err.println("Server: 서버를 종료합니다.");
                System.err.println(">>> 프로그램이 종료됩니다.");
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (!server.isTerminated()) {
                    server.shutdown();
                }
            }));

            try {
                server.awaitTermination();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        serverThread.start();
    }
}
