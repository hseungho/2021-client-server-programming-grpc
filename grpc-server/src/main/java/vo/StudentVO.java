package vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentVO {
    private Long id;
    private String studentId;
    private String firstName;
    private String lastName;
    private String department;
    private Set<CourseVO> completedCourses;

}
