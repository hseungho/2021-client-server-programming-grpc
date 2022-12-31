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
    public void addCourse(ClientServer.Course request, StreamObserver<ClientServer.Status> responseObserver) {
        ClientServer.Status response = serverGrpc.addCourse(request);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void addStudent(ClientServer.Student request, StreamObserver<ClientServer.Status> responseObserver) {
        ClientServer.Status response = serverGrpc.addStudent(request);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteStudent(ClientServer.Id request, StreamObserver<ClientServer.Status> responseObserver) {
        ClientServer.Status response = serverGrpc.deleteStudent(request);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteCourse(ClientServer.Id request, StreamObserver<ClientServer.Status> responseObserver) {
        ClientServer.Status response = serverGrpc.deleteCourse(request);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void register(ClientServer.Register request, StreamObserver<ClientServer.Status> responseObserver) {
        ClientServer.Status response = serverGrpc.register(request);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllRegisterData(ClientServer.Id request, StreamObserver<ClientServer.CourseList> responseObserver) {
        ClientServer.CourseList response = serverGrpc.getAllRegisterOfStudent(request);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
