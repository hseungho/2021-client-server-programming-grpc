package entity;

import dto.request.CourseCreateRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

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

    // TODO OneToMany -> ManyToMany -> Entity 승격?
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

    public Course(String courseId, String profName, String courseName) {
        this.courseId = courseId;
        this.profName = profName;
        this.courseName = courseName;
    }
    public static Course createEntity(CourseCreateRequest courseCreateRequest) {
        return new Course(
                courseCreateRequest.getCourseId(),
                courseCreateRequest.getProfName(),
                courseCreateRequest.getCourseName()
        );
    }

    public void validatePrerequisite(Set<Course> completedCourseList) {
        if(!completedCourseList.contains(this)) {

        }
    }

    @Override
    public boolean equals(Object other) {
        if(this==other) return true;
        return other instanceof Course c && Objects.equals(this.id, c.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
