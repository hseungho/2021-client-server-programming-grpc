package repository;

import entity.Student;

import java.util.Optional;

public class StudentRepository extends Repository<Student, Long> {

    public StudentRepository() {
        super(Student.class);
    }

    public Optional<Student> findByStudentId(String studentId) {
        return super.findByStringCondition("studentId", studentId);
    }

    public void deleteByStudentId(String studentId) {
        super.deleteByStringCondition("studentId", studentId);
    }

}
