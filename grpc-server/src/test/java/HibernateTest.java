import entity.Student;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repository.StudentRepository;

public class HibernateTest {

    private static StudentRepository studentRepository;

    @BeforeAll
    public static void init() {
        studentRepository = new StudentRepository();
    }
    @Test
    public void findStudentById() {
        Student student = studentRepository.findById(1L);
        System.out.println(student);
    }
}
