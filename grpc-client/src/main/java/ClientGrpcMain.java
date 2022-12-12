import java.io.IOException;

public class ClientGrpcMain {
    public static void main(String[] args) throws IOException {
        ClientGrpc client = ClientGrpc.getInstance();
        client.start();
    }
}
