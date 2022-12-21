package config;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class HibernateConfig {

    private static HibernateConfig instance;
    public static HibernateConfig getInstance() {
        if(instance==null)
            instance = new HibernateConfig();
        return instance;
    }

    private static SessionFactory sessionFactory;
    public HibernateConfig() {
        sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();
    }

    @PersistenceContext
    private EntityManager em;
    public static EntityManager entityManager() {
        return sessionFactory.createEntityManager();
    }

    public <T> T findById(Class<T> type, Long id) {
        em = HibernateConfig.entityManager();
        em.getTransaction().begin();
        T value = em.find(type, id);
        em.persist(value);
        em.getTransaction().commit();
        return value;
    }

    public <T> void save(T object) {

    }

}
