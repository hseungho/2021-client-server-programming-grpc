import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.netty.NettyServerBuilder;

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

    public ServerGrpc() {
        init();
    }

    private void init() {
        initChannel();
        initStub();
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
        System.out.println("CALLED METHOD: getAllStudentList");
        return stub.getAllStudentData(Empty.newBuilder().build());
    }

    @Override
    public ClientServer.CourseList getAllCourseList() {
        System.out.println("CALLED METHOD: getAllCourseList");
        return stub.getAllCourseData(Empty.newBuilder().build());
    }

    @Override
    public ClientServer.Status addStudent(ClientServer.Student student) {
        System.out.println("CALLED METHOD: addStudent");
        if(isExistStudentId(student.getId())) {
            return ClientServer.Status.newBuilder()
                    .setStatus(409)
                    .setMessage("This student id is already exists.")
                    .build();
        }
        return stub.addStudent(student);
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
        System.out.println("CALLED METHOD: deleteStudent");
        if(!isExistStudentId(studentId.getId())) {
            return ClientServer.Status.newBuilder()
                    .setStatus(409)
                    .setMessage("This student id is not exist.").build();
        }
        return stub.deleteStudent(studentId);
    }

    @Override
    public ClientServer.Status addCourse(ClientServer.Course course) {
        System.out.println("CALLED METHOD: addCourse");
        if(isExistCourseId(course.getId())) {
            return ClientServer.Status.newBuilder()
                    .setStatus(409)
                    .setMessage("This course id is already exists.")
                    .build();
        }
        return stub.addCourse(course);
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
