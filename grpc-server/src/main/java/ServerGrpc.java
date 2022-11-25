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
        System.out.println("LOG INFO: get all student list");
        return stub.getAllStudentData(Empty.newBuilder().build());
    }

    @Override
    public ClientServer.CourseList getAllCourseList() {
        System.out.println("LOG INFO: get all course list");
        return stub.getAllCourseData(Empty.newBuilder().build());
    }

    @Override
    public ClientServer.Status addStudent(ClientServer.Student student) {
        System.out.println("LOG INFO: add student");
        if(isExistStudentId(student.getId())) {
            return ClientServer.Status.newBuilder()
                    .setStatus(409)
                    .setMessage("This student id is already exists.")
                    .build();
        }
        return stub.addStudent(student);
    }

    @Override
    public ClientServer.StudentIdList getStudentIdList() {
        System.out.println("LOG INFO: get student id list");
        return stub.getStudentIdList(Empty.newBuilder().build());
    }

    private boolean isExistStudentId(String id) {
        ClientServer.StudentIdList studentIdList = getStudentIdList();
        List<String> studentIds = new ArrayList<>(studentIdList.getIdList());
        return studentIds.contains(id);
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
