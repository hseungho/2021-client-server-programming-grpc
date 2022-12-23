package repository;

import config.Repository;
import entity.Student;

import java.util.Optional;

public class StudentRepository extends Repository<Student, Long> {

    public StudentRepository() {
        super(Student.class);
    }

    public Optional<Student> findByStudentId(String studentId) {
        return super.findByStringField("studentId", studentId);
    }

}
