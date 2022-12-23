import entity.Student;
import vo.StudentVO;

import java.util.List;
import java.util.stream.Collectors;

public class StudentDtoConverter {

    public static ClientServer.StudentList toProtoStudentList(List<Student> students) {
        ClientServer.StudentList protoStudentList;
        List<ClientServer.Student> protoStudents = students.stream()
                .map(StudentDtoConverter::toProtoStudent).toList();
        protoStudentList = ClientServer.StudentList.newBuilder()
                .setStatus("SUCCESS")
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

    public static StudentVO toVO(ClientServer.Student studentDto) {
        return new StudentVO(
                studentDto.getId(),
                studentDto.getStudentId(),
                studentDto.getFirstName(),
                studentDto.getLastName(),
                studentDto.getDepartment(),
                studentDto.getCompletedCourseListList().stream()
                        .map(CourseDtoConverter::toVO).collect(Collectors.toSet())
        );
    }

}
