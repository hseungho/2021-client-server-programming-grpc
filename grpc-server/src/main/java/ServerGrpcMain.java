public class ServerGrpcMain {
    public static void main(String[] args) {
        ServerGrpc server = ServerGrpc.getInstance();
        server.start();
    }
}
