package applicationservice;

import entity.Course;
import exception.LMSException;
import exception.NotFoundCourseIdException;
import exception.NotFoundStudentIdException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repository.CourseRepository;
import repository.StudentRepository;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RegisterServiceTest {

    static RegisterService registerService;

    @BeforeAll
    static void init() {
        registerService = new RegisterService(new StudentRepository(), new CourseRepository());
    }
    @Test
    @Transactional
    void register() {
        String studentId = "20130095";
        String courseId = "17655";

        registerService.register(studentId, courseId);

        List<Course> completedCourses = registerService.getAllRegisterCourseOfStudent(studentId);
        completedCourses.forEach(System.out::println);

        registerService.deleteRegisterForRollback(studentId, courseId);
    }

    @Test
    void registerWrongStudent() {
        String studentId = "201312";
        String courseId = "17655";

        assertThrows(NotFoundStudentIdException.class,
                () -> registerService.register(studentId, courseId));
    }

    @Test
    void registerWrongCourse() {
        String studentId = "20130095";
        String courseId = "19519";

        assertThrows(NotFoundCourseIdException.class,
                () -> registerService.register(studentId, courseId));
    }

    @Test
    void registerAlreadyCompletedCourse() {
        String studentId = "20130095";
        String courseId = "12345";

        assertEquals("this course has already been taken.", assertThrows(LMSException.class,
                () -> registerService.register(studentId, courseId)).getMessage());
    }

    @Test
    void registerNotCompletedPrerequisite() {
        String studentId = "20130094";
        String courseId = "17655";

        assertEquals("Have not completed prerequisite course of this course.", assertThrows(LMSException.class,
                () -> registerService.register(studentId, courseId)).getMessage());
    }
}