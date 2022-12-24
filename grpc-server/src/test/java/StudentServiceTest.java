import applicationservice.StudentService;
import entity.Student;
import exception.MyException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repository.CourseRepository;
import repository.StudentRepository;
import vo.StudentVO;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    @Transactional
    public void 영속성_체크를_위한_학생_이름_변경_및_확인() {
        Student student_1 = studentService.getStudentById(1L);
        student_1.setLastName("Update_test");

        Student student_2 = studentService.getStudentById(1L);
        assertEquals(student_1.getLastName(), student_2.getLastName());
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
                .map(CourseDtoConverter::toVO)
                .collect(Collectors.toSet()));
        try {
            studentService.addStudent(student_1);
        } catch (MyException.DuplicationDataException e) {
            System.err.println(e.getMessage());
        }
        Student student_2 = studentService.getStudentByStudentId("20221222");
        assertEquals(student_1.getStudentId(), student_2.getStudentId());

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
        assertThrows(MyException.InvalidedDataException.class, () -> {
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