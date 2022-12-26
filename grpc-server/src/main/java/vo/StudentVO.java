package vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class StudentVO {
    private Long id;
    private String studentId;
    private String firstName;
    private String lastName;
    private String department;
    private Set<CourseVO> completedCourses;

}
