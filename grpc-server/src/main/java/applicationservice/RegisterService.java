package applicationservice;

import entity.Course;
import entity.Student;
import exception.NotFoundCourseIdException;
import exception.NotFoundStudentIdException;
import repository.CourseRepository;
import repository.StudentRepository;

import java.util.Comparator;
import java.util.List;

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

        student.register(course);

        this.studentRepository.save(student);
    }

    public List<Course> getAllRegisterCourseOfStudent(String studentId) {
        Student student = studentRepository.findByStudentId(studentId).orElseThrow(NotFoundStudentIdException::new);
        return student.getCompletedCourseList().stream().sorted(Comparator.comparing(Course::getCourseId)).toList();
    }

    public void deleteRegisterForRollback(String studentId, String courseId) {
        Student student = studentRepository.findByStudentId(studentId).orElseThrow(NotFoundStudentIdException::new);
        Course course = courseRepository.findByCourseId(courseId).orElseThrow(NotFoundCourseIdException::new);
        student.getCompletedCourseList().remove(course);
        studentRepository.save(student);
    }
}
