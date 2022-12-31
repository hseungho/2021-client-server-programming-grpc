package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {
    private Long id;
    private String courseId;
    private String profName;
    private String courseName;
    private List<String> prerequisiteIds;
}
