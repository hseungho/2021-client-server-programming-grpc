package config;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class HibernateFactory {

    private static HibernateFactory instance;
    public static HibernateFactory getInstance() {
        if(instance==null)
            instance = new HibernateFactory();
        return instance;
    }

    private static SessionFactory sessionFactory;
    @PersistenceContext
    private final EntityManager em;
    public HibernateFactory() {
        sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();
        em = sessionFactory.createEntityManager();
    }

    public EntityManager entityManager() {
        return em;
    }

}
