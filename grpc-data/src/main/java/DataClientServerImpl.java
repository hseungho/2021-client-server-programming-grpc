import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;

public final class DataClientServerImpl extends ClientServerProtoGrpc.ClientServerProtoImplBase {

    private final DataGrpc dataGrpc = DataGrpc.getInstance();

    @Override
    public void getAllStudentData(Empty request, StreamObserver<ClientServer.StudentList> responseObserver) {
        ClientServer.StudentList studentListResponse = dataGrpc.getAllStudentList();
        responseObserver.onNext(studentListResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllCourseData(Empty request, StreamObserver<ClientServer.CourseList> responseObserver) {
        ClientServer.CourseList courseListResponse = dataGrpc.getAllCourseList();
        responseObserver.onNext(courseListResponse);
        responseObserver.onCompleted();
    }

}
