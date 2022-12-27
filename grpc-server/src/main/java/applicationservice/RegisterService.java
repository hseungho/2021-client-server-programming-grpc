package applicationservice;

import entity.Course;
import entity.Student;
import exception.NotFoundCourseIdException;
import exception.NotFoundStudentIdException;
import repository.CourseRepository;
import repository.StudentRepository;

public class RegisterService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public RegisterService(StudentRepository studentRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    public void register(String studentId, String courseId) {
        Student student = studentRepository.findByStudentId(studentId).orElseThrow(NotFoundStudentIdException::new);
        Course course = courseRepository.findByCourseId(courseId).orElseThrow(NotFoundCourseIdException::new);

    }
}
