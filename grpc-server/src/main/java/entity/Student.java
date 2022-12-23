package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vo.StudentVO;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "student")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "student_id")
    private String studentId;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private String department;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "student_course",
            joinColumns = @JoinColumn(name = "s_id"),
            inverseJoinColumns = @JoinColumn(name = "c_id")
    )
    private List<Course> completedCourseList;

    public Student(String studentId, String firstName, String lastName, String department, List<Course> completedCourseList) {
        super();
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;
        this.completedCourseList = completedCourseList;
    }

    public static Student create(StudentVO studentVo) {
        return new Student(
                studentVo.getStudentId(),
                studentVo.getFirstName(),
                studentVo.getLastName(),
                studentVo.getDepartment(),
                studentVo.getCompletedCourses()
        );
    }
}
