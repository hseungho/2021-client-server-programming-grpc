import applicationservice.CourseService;
import applicationservice.RegisterService;
import applicationservice.StudentService;
import dto.request.CourseCreateRequest;
import dto.request.StudentCreateRequest;
import entity.Course;
import entity.Student;
import exception.LMSException;
import exception.NotFoundCourseIdException;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.netty.NettyServerBuilder;
import repository.CourseRepository;
import repository.StudentRepository;

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
    private RegisterService registerService;

    public ServerGrpc() {
        init();
    }

    private void init() {
        initChannel();
        initStub();
        initService();
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

    private void initService() {
        StudentRepository studentRepository = new StudentRepository();
        CourseRepository courseRepository = new CourseRepository();
        studentService = new StudentService(studentRepository, courseRepository);
        courseService = new CourseService(courseRepository);
        registerService = new RegisterService(studentRepository, courseRepository);
    }

    public void start() {
        System.out.println("<<<<< SERVER START >>>>>");
        this.startServer();
    }

    public ClientServer.StudentList getAllStudentList() {
        info("getAllStudentList");
        List<Student> students = studentService.getAllStudentList();
        return StudentDtoConverter.toProtoStudentList(students);
    }

    public ClientServer.CourseList getAllCourseList() {
        info("getAllCourseList");
        List<Course> courses = courseService.getAllCourseList();
        return CourseDtoConverter.toProtoCourseList(courses);
    }

    public ClientServer.Status addStudent(ClientServer.Student studentDto) {
        info("addStudent [ID: "+studentDto.getStudentId()+"]");
        StudentCreateRequest studentCreateRequest = StudentDtoConverter.toCreateRequest(studentDto);
        try {
            studentService.addStudent(studentCreateRequest);
        } catch (NotFoundCourseIdException e) {
            System.err.println("LOG: request invalid course ID");
            return ClientServer.Status.newBuilder()
                    .setCode(HttpResponseCode.NOT_FOUND.getCode())
                    .setMessage(e.getMessage())
                    .build();
        } catch (LMSException e) {
            System.err.printf("LOG: create duplicate student, request id: %s\n", studentCreateRequest.getStudentId());
            return ClientServer.Status.newBuilder()
                    .setCode(HttpResponseCode.BAD_REQUEST.getCode())
                    .setMessage(e.getMessage())
                    .build();
        } 
        return ClientServer.Status.newBuilder()
                .setCode(HttpResponseCode.CREATED.getCode())
                .setMessage("ADD STUDENT SUCCESS !!!")
                .build();
    }

    public ClientServer.Status addCourse(ClientServer.Course courseDto) {
        info("addCourse [ID: "+courseDto.getCourseId() +"]");
        CourseCreateRequest courseCreateRequest = CourseDtoConverter.toCreateRequest(courseDto);
        try {
            courseService.addCourse(courseCreateRequest);
        } catch (LMSException e) {
            System.err.printf("LOG: create duplicate course, request id: %s\n", courseCreateRequest.getCourseId());
            return ClientServer.Status.newBuilder()
                    .setCode(HttpResponseCode.BAD_REQUEST.getCode())
                    .setMessage(e.getMessage())
                    .build();
        }
        return ClientServer.Status.newBuilder()
                .setCode(HttpResponseCode.CREATED.getCode())
                .setMessage("ADD COURSE SUCCESS !!!")
                .build();
    }

    public ClientServer.Status deleteStudent(ClientServer.Id studentId) {
        info("deleteStudent [ID: " +studentId.getId() +"]");
        try {
            studentService.deleteStudent(studentId.getId());
        } catch (LMSException e) {
            return ClientServer.Status.newBuilder()
                    .setCode(HttpResponseCode.BAD_REQUEST.getCode())
                    .setMessage(e.getMessage())
                    .build();
        }
        return ClientServer.Status.newBuilder()
                .setCode(HttpResponseCode.OK.getCode())
                .setMessage("DELETE STUDENT SUCCESS !!!")
                .build();
    }

    public ClientServer.Status deleteCourse(ClientServer.Id courseId) {
        info("deleteCourse [ID: "+courseId.getId()+"]");
        try {
            courseService.deleteCourse(courseId.getId());
        } catch (LMSException e) {
            return ClientServer.Status.newBuilder()
                    .setCode(HttpResponseCode.BAD_REQUEST.getCode())
                    .setMessage(e.getMessage())
                    .build();
        }
        return ClientServer.Status.newBuilder()
                .setCode(HttpResponseCode.OK.getCode())
                .setMessage("DELETE COURSE SUCCESS !!!")
                .build();
    }

    public ClientServer.Status register(ClientServer.Register registerDto) {
        info(String.format("register [S_ID: %s, C_ID: %s]", registerDto.getStudentId(), registerDto.getCourseId()));
        try {
            registerService.register(registerDto.getStudentId(), registerDto.getCourseId());
        } catch (LMSException e) {
            return ClientServer.Status.newBuilder()
                    .setCode(HttpResponseCode.BAD_REQUEST.getCode())
                    .setMessage(e.getMessage())
                    .build();
        }
        return ClientServer.Status.newBuilder()
                .setCode(HttpResponseCode.OK.getCode())
                .setMessage("REGISTER SUCCESS !!!")
                .build();
    }

    public ClientServer.CourseList getAllRegisterOfStudent(ClientServer.Id studentId) {
        info(String.format("getAllRegisterOfStudent [S_ID: %s]", studentId.getId()));
        try {
            List<Course> allRegisterCourseOfStudent = registerService.getAllRegisterCourseOfStudent(studentId.getId());

            return CourseDtoConverter.toProtoCourseList(allRegisterCourseOfStudent);

        } catch (LMSException e) {
            return ClientServer.CourseList.newBuilder()
                    .setStatus(ClientServer.Status.newBuilder()
                            .setCode(HttpResponseCode.BAD_REQUEST.getCode())
                            .setMessage(e.getMessage())
                            .build())
                    .build();
        }
    }

    private static void info(String log) {
        System.out.println("\nLOG:" +log);
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
