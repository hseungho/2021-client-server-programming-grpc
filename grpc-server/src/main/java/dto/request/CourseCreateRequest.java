package dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CourseCreateRequest {
    private String courseId;
    private String profName;
    private String courseName;
    private List<String> prerequisiteIds;
}
