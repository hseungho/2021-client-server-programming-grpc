import applicationservice.StudentService;
import entity.Student;
import exception.MyException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import repository.CourseRepository;
import vo.StudentVO;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StudentServiceTest {

    private static StudentService studentService;

    @BeforeAll
    public static void init() {
        studentService = new StudentService();
    }

    @Test
    @DisplayName("test of find all list from database")
    void findAll() {
        List<Student> studentList = studentService.getAllStudentList();
//        System.out.println(students);
        assertEquals("20100123", studentList.get(0).getStudentId());
    }

    @Test
    @DisplayName("find student by id test")
    public void findStudentById() {
        Student student_1 = new Student();
        student_1.setStudentId("20100123");
        student_1.setFirstName("Hwang");
        student_1.setLastName("Myunghan");
        student_1.setDepartment("CS");

        Student student_2 = studentService.getStudentById(1L);

        assertEquals(student_2.getStudentId(), student_1.getStudentId());
        assertEquals(student_2.getFirstName(), student_1.getFirstName());
        assertEquals(student_2.getLastName(), student_1.getLastName());
        assertEquals(student_2.getDepartment(), student_1.getDepartment());
    }

    @Test
    @Transactional
    @DisplayName("check persistence of hibernate")
    public void updateStudentForCheckPersistence() {
        Student student_1 = studentService.getStudentById(1L);
        student_1.setLastName("Update_test");

        Student student_2 = studentService.getStudentById(1L);
        assertEquals(student_1.getLastName(), student_2.getLastName());
    }

    @Test
    @DisplayName("test of find student by student id")
    public void findStudentByStudentId() {
        Student student;
        student = studentService.getStudentByStudentId("20100123");
        assertEquals("20100123", student.getStudentId());
    }

    @Test
    @Transactional
    @DisplayName("test of create student")
    void create() {
        // case success
        StudentVO student_1 = new StudentVO();
        student_1.setStudentId("20221222");
        student_1.setFirstName("Hwang");
        student_1.setLastName("Seungho");
        student_1.setDepartment("ICT");

        List<String> courseIds = List.of("12345", "23456", "17651");
        CourseRepository courseRepository = new CourseRepository();
        student_1.setCompletedCourses(courseIds.stream()
                .map(courseRepository::findByCourseId)
                .map(Optional::orElseThrow)
                .toList());
        try {
            studentService.addStudent(student_1);
        } catch (MyException.DuplicationDataException e) {
            System.err.println(e.getMessage());
        }
        Student student_2 = studentService.getStudentByStudentId("20221222");
        assertEquals(student_1.getStudentId(), student_2.getStudentId());
    }

    @Test
    void delete() {
    }
}