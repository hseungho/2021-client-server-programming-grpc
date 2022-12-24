package applicationservice;

import entity.Student;
import exception.MyException;
import repository.StudentRepository;
import vo.StudentVO;

import java.util.List;

public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService() {
        this.studentRepository = new StudentRepository();
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
        studentRepository.save(student);
    }

    public void deleteStudent(String studentId) {
        if(studentRepository.findByStudentId(studentId).isEmpty()) {
            throw new MyException.InvalidedDataException("This student id doesn't exist");
        }
        studentRepository.deleteByStudentId(studentId);
    }


}
