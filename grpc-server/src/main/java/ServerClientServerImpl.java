import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;

public final class ServerClientServerImpl extends ClientServerProtoGrpc.ClientServerProtoImplBase {

    private final ServerGrpc serverGrpc = ServerGrpc.getInstance();

    @Override
    public void getAllStudentData(Empty request, StreamObserver<ClientServer.StudentList> responseObserver) {
        ClientServer.StudentList response = serverGrpc.getAllStudentList();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
