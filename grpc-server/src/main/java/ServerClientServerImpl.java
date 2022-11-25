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

    @Override
    public void getAllCourseData(Empty request, StreamObserver<ClientServer.CourseList> responseObserver) {
        ClientServer.CourseList response = serverGrpc.getAllCourseList();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void addStudent(ClientServer.Student request, StreamObserver<ClientServer.Status> responseObserver) {


    }

}
