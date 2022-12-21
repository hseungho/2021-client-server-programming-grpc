package repository;

import config.HibernateConfig;
import entity.Student;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class StudentRepository {

    private Session session;
    private Transaction tx;

    public Student findById(Long id) {
        session = HibernateConfig.getSession();
        tx = session.beginTransaction();
        Student student = session.get(Student.class, id);
        tx.commit();
        session.close();
        return student;
    }

}
