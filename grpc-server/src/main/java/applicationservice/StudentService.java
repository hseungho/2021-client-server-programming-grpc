package applicationservice;

import entity.Course;
import entity.Student;
import exception.MyException;
import exception.NotFoundCourseIdException;
import repository.CourseRepository;
import repository.StudentRepository;
import vo.StudentVO;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public StudentService(StudentRepository studentRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    public List<Student> getAllStudentList() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new MyException.NullDataException("Not exist student for this id"));
    }

    public Student getStudentByStudentId(String studentId) {
        return studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new MyException.NullDataException("Not exist student for this student id"));
    }

    public void addStudent(StudentVO studentVo) {
        Student student = Student.createEntity(studentVo);
        addStudent(student);
    }

    public void addStudent(Student student) {
        studentRepository.findByStudentId(student.getStudentId()).stream().findAny()
                .ifPresent(s -> {
                    throw new MyException.DuplicationDataException("This student id is duplicated");
                });
        Set<Course> completedCourses = student.getCompletedCourseList().stream().map(course ->
                courseRepository.findByCourseId(course.getCourseId())
                        .orElseThrow(NotFoundCourseIdException::new)
        ).collect(Collectors.toSet());
        student.setCompletedCourseList(completedCourses);
        studentRepository.save(student);
    }

    public void deleteStudent(String studentId) {
        try {
            studentRepository.deleteByStudentId(studentId);
        } catch (MyException.NullDataException e) {
            System.err.println("LOG: "+e.getMessage());
            throw new MyException.InvalidedDataException("This student id doesn't exist");
        }
    }


}
