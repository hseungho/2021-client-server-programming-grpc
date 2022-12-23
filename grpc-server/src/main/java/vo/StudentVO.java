package vo;

import entity.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentVO {
    private Long id;
    private String studentId;
    private String firstName;
    private String lastName;
    private String department;
    private List<Course> completedCourses;

}
