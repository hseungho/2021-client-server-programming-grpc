package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vo.StudentVO;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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

    // TODO Entity 승격?
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "student_course",
            joinColumns = @JoinColumn(name = "s_id"),
            inverseJoinColumns = @JoinColumn(name = "c_id")
    )
    private Set<Course> completedCourseList;

    public Student(String studentId, String firstName, String lastName, String department, Set<Course> completedCourseList) {
        super();
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;
        this.completedCourseList = completedCourseList;
    }

    private Student(Student c_student) {
        this.studentId = c_student.getStudentId();
        this.firstName = c_student.getFirstName();
        this.lastName = c_student.getLastName();
        this.department = c_student.getDepartment();
        this.completedCourseList = new HashSet<>(c_student.getCompletedCourseList());
    }

    public static Student newInstance(Student student) {
        return new Student(student);
    }

    public static Student createEntity(StudentVO studentVo) {
        return new Student(
                studentVo.getId(),
                studentVo.getStudentId(),
                studentVo.getFirstName(),
                studentVo.getLastName(),
                studentVo.getDepartment(),
                studentVo.getCompletedCourses().stream()
                        .map(Course::createEntity).collect(Collectors.toSet())
        );
    }
}
