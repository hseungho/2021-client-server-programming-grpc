import dto.request.StudentCreateRequest;
import entity.Student;

import java.util.List;

public class StudentDtoConverter {

    public static ClientServer.StudentList toProtoStudentList(List<Student> students) {
        ClientServer.StudentList protoStudentList;

        List<ClientServer.Student> protoStudents = students.stream()
                .map(StudentDtoConverter::toProtoStudent).toList();

        protoStudentList = ClientServer.StudentList.newBuilder()
                .setStatus(ClientServer.Status.newBuilder()
                        .setCode(HttpResponseCode.OK.getCode())
                        .build())
                .addAllStudent(protoStudents)
                .build();

        return protoStudentList;
    }

    public static ClientServer.Student toProtoStudent(Student student) {
        return ClientServer.Student.newBuilder()
                .setId(student.getId())
                .setStudentId(student.getStudentId())
                .setFirstName(student.getFirstName())
                .setLastName(student.getLastName())
                .setDepartment(student.getDepartment())
                .addAllCompletedCourseList(
                        student.getCompletedCourseList().stream()
                                .map(CourseDtoConverter::toProtoCourse)
                                .toList()
                )
                .build();
    }

    public static StudentCreateRequest toCreateRequest(ClientServer.Student studentDto) {
        return new StudentCreateRequest(
                studentDto.getStudentId(),
                studentDto.getFirstName(),
                studentDto.getLastName(),
                studentDto.getDepartment(),
                studentDto.getCompletedCourseListList().stream()
                        .map(ClientServer.Course::getCourseId)
                        .toList()
        );
    }

}
