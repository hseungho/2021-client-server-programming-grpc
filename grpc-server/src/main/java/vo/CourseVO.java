package vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class CourseVO {
    private Long id;
    private String courseId;
    private String profName;
    private String courseName;
    private Set<CourseVO> prerequisite;

}
