package entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "course")
@Data
@NoArgsConstructor
public class Course {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String courseId;
    private String profName;
    private String courseName;
    @OneToMany(fetch = FetchType.LAZY)
    private List<Course> prerequisite;

    public Course(String courseId, String profName, String courseName, List<Course> prerequisite) {
        super();
        this.courseId = courseId;
        this.profName = profName;
        this.courseName = courseName;
        this.prerequisite = prerequisite;
    }

}
