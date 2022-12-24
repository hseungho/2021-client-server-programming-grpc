import applicationservice.CourseService;
import applicationservice.StudentService;
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
import java.util.List;

public class ServerGrpc {

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

    public ClientServer.StudentList getAllStudentList() {
        System.out.println("LOG: getAllStudentList");
        List<Student> students = studentService.getAllStudentList();
        return StudentDtoConverter.toProtoStudentList(students);
    }

    public ClientServer.CourseList getAllCourseList() {
        System.out.println("CALLED METHOD: getAllCourseList");
        List<Course> courses = courseService.getAllCourseList();
        return CourseDtoConverter.toProtoCourseList(courses);
    }

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

    public ClientServer.Status deleteStudent(ClientServer.Id studentId) {
        System.out.println("CALLED METHOD: deleteStudent [ID: "+studentId.getId()+"]");
        try {
            studentService.deleteStudent(studentId.getId());
        } catch (MyException.InvalidedDataException e) {
            return ClientServer.Status.newBuilder()
                    .setStatus(409)
                    .setMessage(e.getMessage())
                    .build();
        }
        return ClientServer.Status.newBuilder().setStatus(200).build();
    }

    public ClientServer.Status deleteCourse(ClientServer.Id courseId) {
        System.out.println("CALLED METHOD: deleteCourse [ID: "+courseId.getId()+"]");
        try {
            courseService.deleteCourse(courseId.getId());
        } catch (MyException.InvalidedDataException e) {
            return ClientServer.Status.newBuilder()
                    .setStatus(409)
                    .setMessage(e.getMessage())
                    .build();
        }
        return ClientServer.Status.newBuilder().setStatus(200).build();
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
