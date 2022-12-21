import entity.Student;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import repository.StudentRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HibernateTest {

    private static StudentRepository studentRepository;

    @BeforeAll
    public static void init() {
        studentRepository = new StudentRepository();
    }
    @Test
    @DisplayName("find student by id test")
    public void findStudentById() {
        Student student_1 = new Student();
        student_1.setStudentId("20100123");
        student_1.setFirstName("Hwang");
        student_1.setLastName("Myunghan");
        student_1.setDepartment("CS");

        Student student_2 = studentRepository.findById(1L);

        assertEquals(student_2.getStudentId(), student_1.getStudentId());
        assertEquals(student_2.getFirstName(), student_1.getFirstName());
        assertEquals(student_2.getLastName(), student_1.getLastName());
        assertEquals(student_2.getDepartment(), student_1.getDepartment());
    }
}
