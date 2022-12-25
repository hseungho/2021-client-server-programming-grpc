package vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseVO {
    private Long id;
    private String courseId;
    private String profName;
    private String courseName;
    private Set<CourseVO> prerequisite;

}
