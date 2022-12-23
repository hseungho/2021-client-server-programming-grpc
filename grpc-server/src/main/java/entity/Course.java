package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vo.CourseVO;

import javax.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "course")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "course_id")
    private String courseId;
    @Column(name = "prof_name")
    private String profName;
    @Column(name = "course_name")
    private String courseName;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinTable(
            name = "prerequisite",
            joinColumns = @JoinColumn(name = "c_id"),
            inverseJoinColumns = @JoinColumn(name = "pre_c_id")
    )
    private Set<Course> prerequisite;

    public Course(String courseId, String profName, String courseName, Set<Course> prerequisite) {
        super();
        this.courseId = courseId;
        this.profName = profName;
        this.courseName = courseName;
        this.prerequisite = prerequisite;
    }

    public static Course createEntity(CourseVO courseVO) {
        return new Course(
                courseVO.getId(),
                courseVO.getCourseId(),
                courseVO.getProfName(),
                courseVO.getCourseName(),
                courseVO.getPrerequisite().stream()
                        .map(Course::createEntity).collect(Collectors.toSet())
        );
    }
}
