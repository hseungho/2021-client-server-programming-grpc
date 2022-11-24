import io.grpc.Server;
import io.grpc.netty.NettyServerBuilder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

public class DataGrpc {
    public static DataGrpc instance;
    public static DataGrpc getInstance() {
        return instance != null ? instance : new DataGrpc();
    }

    private Server server;
    private static StudentList studentList;

    public DataGrpc() {
        try {
            studentList = new StudentList("Students.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        System.out.println("<<<<< DATA SERVER START >>>>>");
        startServer();
    }

    public List<Student> getAllStudentList() throws MyException.NullDataException {
        System.out.println("CALLED: getAllStudentList()");
        return studentList.getAllStudentRecords();
    }

    private void startServer() {
        Thread serverThread = new Thread(() -> {
            int port = 9090;
            server = NettyServerBuilder
                    .forAddress(new InetSocketAddress("localhost", port))
                    .addService(new DataClientServerImpl())
                    .build();

            try {
                server.start();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.err.println("\nData Server: gRPC 서버를 종료합니다.");
                System.err.println("Data Server: 서버를 종료합니다.");
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
