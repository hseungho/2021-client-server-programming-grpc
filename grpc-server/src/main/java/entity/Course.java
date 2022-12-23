package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

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

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "prerequisite",
            joinColumns = @JoinColumn(name = "c_id"),
            inverseJoinColumns = @JoinColumn(name = "pre_c_id")
    )
    private List<Course> prerequisite;

    public Course(String courseId, String profName, String courseName, List<Course> prerequisite) {
        super();
        this.courseId = courseId;
        this.profName = profName;
        this.courseName = courseName;
        this.prerequisite = prerequisite;
    }

}
