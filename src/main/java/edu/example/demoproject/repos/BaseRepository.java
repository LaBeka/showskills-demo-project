package edu.example.demoproject.repos;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public abstract class BaseRepository<T, L extends Number> {

    @PersistenceContext
    protected EntityManager em;

    public T persist(T e) {
        em.persist(e);
        return e;
    }

    public T merge(T e){
        em.merge(e);
        return e;
    }

    public T findById(Class<T> clazz, L id) {
        return em.find(clazz, id);
    }

    public void remove(T e) {
        em.remove(e);
    }
}

