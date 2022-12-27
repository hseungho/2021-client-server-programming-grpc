package dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudentCreateRequest {
    private String studentId;
    private String firstName;
    private String lastName;
    private String department;
    private List<String> completedCourses;

}
