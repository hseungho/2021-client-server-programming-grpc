package entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "student")
@Data
@NoArgsConstructor
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
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "student_course")
//    private List<Course> completedCourseList;

    public Student(String studentId, String firstName, String lastName, String department, List<Course> completedCourseList) {
        super();
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;
//        this.completedCourseList = completedCourseList;
    }

}
