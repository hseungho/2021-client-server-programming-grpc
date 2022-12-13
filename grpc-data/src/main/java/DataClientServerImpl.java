import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.io.IOException;

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

    @Override
    public void getStudentIdList(Empty request, StreamObserver<ClientServer.IdList> responseObserver) {
        ClientServer.IdList response = dataGrpc.getStudentIdList();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getCourseIdList(Empty request, StreamObserver<ClientServer.IdList> responseObserver) {
        ClientServer.IdList response = dataGrpc.getCourseIdList();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void addStudent(ClientServer.Student request, StreamObserver<ClientServer.Status> responseObserver) {
        ClientServer.Status response;
        try {
            response = dataGrpc.addStudent(request);
        } catch (IOException e) {
            response = ClientServer.Status.newBuilder().setStatus(503).build();
        }
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void addCourse(ClientServer.Course request, StreamObserver<ClientServer.Status> responseObserver) {
        ClientServer.Status responseStatus;
        try {
            responseStatus = dataGrpc.addCourse(request);
        } catch (IOException e) {
            responseStatus = ClientServer.Status.newBuilder().setStatus(503).build();
        }
        responseObserver.onNext(responseStatus);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteStudent(ClientServer.Id request, StreamObserver<ClientServer.Status> responseObserver) {
        ClientServer.Status responseStatus;
        try {
            responseStatus = dataGrpc.deleteStudent(request);
        } catch (IOException e) {
            responseStatus = ClientServer.Status.newBuilder().setStatus(503).build();
        }
        responseObserver.onNext(responseStatus);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteCourse(ClientServer.Id request, StreamObserver<ClientServer.Status> responseObserver) {
        ClientServer.Status responseStatus;
        try {
            responseStatus = dataGrpc.deleteCourse(request);
        } catch (IOException e) {
            responseStatus = ClientServer.Status.newBuilder().setStatus(503).build();
        }
        responseObserver.onNext(responseStatus);
        responseObserver.onCompleted();
    }
}
