package org.example.dao;

import org.example.domain.Student;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class StudentDao {

    @PersistenceContext
    private EntityManager em;

    public Student get(int id) {
        return em.find(Student.class, id);
    }

    public Student insert(Student student) {
        em.persist(student);
        return student;
    }
}
