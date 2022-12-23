package applicationservice;

import entity.Student;
import exception.MyException;
import repository.StudentRepository;
import vo.StudentVO;

import java.util.List;

public class StudentService {

    private StudentRepository studentRepository;

    public StudentService() {
        this.studentRepository = new StudentRepository();
    }

    public List<Student> getAllStudentList() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElseThrow();
    }

    public Student getStudentByStudentId(String studentId) {
        return studentRepository.findByStudentId(studentId).orElseThrow();
    }

    public void addStudent(StudentVO studentVo) throws MyException.DuplicationDataException {
        try {
            studentRepository.findByStudentId(studentVo.getStudentId())
                    .ifPresent(s -> {
                        throw new RuntimeException();
                    });
        } catch (RuntimeException e) {
            throw new MyException.DuplicationDataException("This student id is duplicated");
        }
        Student student = Student.create(studentVo);
        studentRepository.save(student);
    }

    public void delete() {

    }


}
