import applicationservice.StudentService;
import dto.request.StudentCreateRequest;
import entity.Student;
import exception.NotFoundStudentIdException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repository.CourseRepository;
import repository.StudentRepository;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

//@Transactional
class StudentServiceTest {

    private static StudentService studentService;

    @BeforeAll
    public static void init() {
        StudentRepository studentRepository = new StudentRepository();
        CourseRepository courseRepository = new CourseRepository();
        studentService = new StudentService(studentRepository, courseRepository);
    }

    @Test
    void 학생데이터_전체조회() {
        List<Student> studentList = studentService.getAllStudentList();
//        System.out.println(students);
        assertEquals("20100123", studentList.get(0).getStudentId());
    }

    @Test
    public void 학생데이터_ID_조회() {
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
    public void 학생데이터_학생_ID_조회() {
        Student student = studentService.getStudentByStudentId("20100123");
        assertEquals("20100123", student.getStudentId());
    }

    @Test
    @Transactional
    void 학생데이터_추가() {
        // case success
        List<String> courseIds = List.of("12345", "23456", "17651");
        StudentCreateRequest new_student = new StudentCreateRequest(
                "20221222", "Hwang", "Seungho", "ICT", courseIds);

        try {
            studentService.addStudent(new_student);
        } catch (MyException.DuplicationDataException e) {
            System.err.println(e.getMessage());
        }
        Student student_2 = studentService.getStudentByStudentId("20221222");
        assertEquals(new_student.getStudentId(), student_2.getStudentId());

        studentService.deleteStudent(student_2.getStudentId()); // rollback
    }

    @Test
    void 학생데이터_삭제() {
        List<Student> students = studentService.getAllStudentList();
        Student del_student = students.get(students.size()-1); // delete student
        Student rb_student = Student.newInstance(del_student); // copy student for rollback

        studentService.deleteStudent(del_student.getStudentId());
        studentService.addStudent(rb_student);

        // 없는 아이디 삭제 요청
        String studentId_2 = "2000000";
        assertThrows(NotFoundStudentIdException.class, () -> {
            studentService.deleteStudent(studentId_2);
        });
    }

    @Test
    void EM_캐시_데이터_조회_여부_확인() {
        Student student_1 = studentService.getStudentById(1L);
        Student student_2 = studentService.getStudentById(1L);

        assertEquals(student_1.getId(), student_2.getId());
    }
}