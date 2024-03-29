package edu.example.demoproject.repos;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public abstract class BaseRepository<T> {

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
}

