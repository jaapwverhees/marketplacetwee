package org.example.dao;

import org.example.domain.Product;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class ProductDao {

    @PersistenceContext
    private EntityManager em;

    public List<Product> readAllProducts() {
        TypedQuery<Product> query = em.createQuery("SELECT p FROM Product p", Product.class);
        return query.getResultList();
    }

    public void create(Product product) {
        em.persist(product);
    }
}
