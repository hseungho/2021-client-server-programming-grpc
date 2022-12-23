package config;

import org.hibernate.Session;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class Repository<T, ID> {
    private final HibernateFactory hibernateFactory = HibernateFactory.getInstance();
    private final EntityManager em = hibernateFactory.entityManager();
    private final Class<T> domainClass;
    public Repository(Class<T> domain) {
        this.domainClass = domain;
    }

    public List<T> findAll() {
        String query = "SELECT a FROM " + domainClass.getName() + " a";
        Session session = em.unwrap(Session.class);
        List<T> valueList = session.createQuery(query, domainClass).getResultList();
        valueList.forEach(v -> {
            if(!em.contains(v)) {
                em.persist(v);
            }
        });
        return valueList;
    }

    public Optional<T> findById(ID id) {
        Session session = em.unwrap(Session.class);
        T value = em.find(domainClass, id);
        if(value == null) {
            value = session.load(domainClass, (Serializable) id);
            em.persist(value);
        }
        return Optional.ofNullable(value);
    }

    public Optional<T> findByStringField(String fieldName, String condition) {
        String query = String.format(
                "SELECT a FROM %s a WHERE a.%s = :condition",
                domainClass.getName(), fieldName
        );
        Session session = em.unwrap(Session.class);
        Optional<T> value = session.createQuery(query, domainClass)
                .setParameter("condition", condition)
                .getResultList().stream().findAny();
        value.ifPresent(v -> {
            if(!em.contains(v)) {
                em.persist(v);
            }
        });
        return value;
    }

    public <S extends T> S save(S entity) {
        em.getTransaction().begin();
        em.merge(entity);
        em.getTransaction().commit();
        return entity;
    }
}
