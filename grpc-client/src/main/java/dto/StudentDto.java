package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class StudentDto {
    private Long id;
    private String studentId;
    private String firstName;
    private String lastName;
    private String department;
    private List<String> completedCourseIds;
}
