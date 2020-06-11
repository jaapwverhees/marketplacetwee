package org.example.dao;

import org.example.domain.Visitor;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class VisitorDoa {

    @PersistenceContext
    private EntityManager em;

    public void create(Visitor visitor){
        em.persist(visitor);
    }

    public Visitor read(String emailAdress){
        return em.find(Visitor.class, emailAdress);
    }

}
