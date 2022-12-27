package dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class StudentDto {
    private Long id;
    private String studentId;
    private String firstName;
    private String lastName;
    private String department;
    private List<String> completedCourseIds;

    public StudentDto(Long id, String studentId, String firstName, String lastName, String department, List<String> completedCourseIds) {
        this.id = id;
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;
        this.completedCourseIds = new ArrayList<>(completedCourseIds);
    }
}
