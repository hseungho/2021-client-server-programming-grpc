public class ClientGrpcMain {
    public static void main(String[] args) {
        ClientGrpc client = ClientGrpc.getInstance();
        client.start();
    }
}
