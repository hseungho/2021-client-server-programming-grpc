import entity.Student;
import vo.StudentVO;

import java.util.List;

public class StudentDtoConverter {

    public static ClientServer.StudentList toProtoStudentListDto(List<Student> students) {
        ClientServer.StudentList protoStudentList;
        List<ClientServer.Student> listProtoStudent = students.stream()
                .map(StudentDtoConverter::toProtoStudentDto).toList();
        protoStudentList = ClientServer.StudentList.newBuilder()
                .setStatus("SUCCESS")
                .addAllStudent(listProtoStudent)
                .build();
        return protoStudentList;
    }

    public static ClientServer.Student toProtoStudentDto(Student student) {
        return ClientServer.Student.newBuilder()
                .setId(student.getId())
                .setStudentId(student.getStudentId())
                .setFirstName(student.getFirstName())
                .setLastName(student.getLastName())
                .setDepartment(student.getDepartment())
                .addAllCompletedCourseList(
                        student.getCompletedCourseList().stream()
                                .map(CourseDtoConverter::toProtoCourseDto)
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
                        .map(CourseDtoConverter::toEntity).toList()
        );
    }

}
