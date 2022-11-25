import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.netty.NettyServerBuilder;

import java.io.IOException;
import java.net.InetSocketAddress;

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
