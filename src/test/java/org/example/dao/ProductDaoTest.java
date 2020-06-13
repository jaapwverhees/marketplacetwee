package org.example.dao;

import org.example.domain.Product;
import org.example.domain.Visitor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ProductDaoTest {

    @Mock
    private TypedQuery<Product> queryProductMock;

    @Mock
    private EntityManager emMock;
    @Mock
    private EntityTransaction entityTransactionMock;

    @InjectMocks
    private ProductDao dao;

    private List<Product> products = new ArrayList<>();

    @Test
    public void verifyEntityManagerPersistIsCalled() {
        doNothing().when(emMock).persist(any());

        dao.create(Product.builder().build());

        verify(emMock).persist(any());
    }

    @Test
    public void verifyCreateQueryIsCalledDuringReadAllProducts(){
        when(emMock.createQuery(anyString(), eq(Product.class))).thenReturn(queryProductMock);

        when(queryProductMock.getResultList()).thenReturn(products);

        dao.readAllProducts();

        verify(emMock).createQuery(anyString(), eq(Product.class));
        verify(queryProductMock).getResultList();
    }
}
