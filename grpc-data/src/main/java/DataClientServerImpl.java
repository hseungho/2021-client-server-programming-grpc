import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;

public final class DataClientServerImpl extends ClientServerProtoGrpc.ClientServerProtoImplBase {

    private final DataGrpc dataGrpc = DataGrpc.getInstance();

    @Override
    public void getAllStudentData(Empty request, StreamObserver<ClientServer.StudentList> responseObserver) {
        ClientServer.StudentList studentListResponse;

        try {
            List<ClientServer.Student> responseList = new ArrayList<>();
            for(Student student : dataGrpc.getAllStudentList()) {
                ClientServer.Student responseStudent = ClientServer.Student.newBuilder()
                                .setId(student.getStudentId())
                                        .setName(student.getName())
                                                .setDepartment(student.getDepartment())
                                                        .build();
                responseList.add(responseStudent);
            }
            studentListResponse = ClientServer.StudentList.newBuilder()
                    .setStatus("SUCCESS")
                    .addAllStudent(responseList)
                    .build();

        } catch (MyException.NullDataException e) {
            studentListResponse = ClientServer.StudentList.newBuilder()
                    .setStatus("FAILED_NO_DATA")
                    .build();
        }
        responseObserver.onNext(studentListResponse);
        responseObserver.onCompleted();
    }
}
