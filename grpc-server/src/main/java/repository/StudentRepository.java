package repository;

import config.HibernateConfig;
import entity.Student;

public class StudentRepository {

    private HibernateConfig hibernate = HibernateConfig.getInstance();

    public Student findById(Long id) {
        return hibernate.findById(Student.class, id);
    }

}
