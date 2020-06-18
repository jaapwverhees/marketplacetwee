package org.example.dao;

import org.example.domain.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

import static org.example.testUtil.Util.giveImageAsByteArray;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductDaoTest {

    private Product product;

    @Mock
    private TypedQuery<Product> queryProductMock;

    @Mock
    private EntityManager emMock;


    @InjectMocks
    private ProductDao dao;

    private List<Product> products = new ArrayList<>();

    @Before
    public void setup() {
        product = Product.builder()
                .name("testProduct")
                .price(22.22)
                .thumbnail(giveImageAsByteArray())
                .build();
    }

    @Test
    public void verifyEntityManagerPersistIsCalled() {
        doNothing().when(emMock).persist(any());

        dao.create(Product.builder().build());

        verify(emMock).persist(any());
    }

    @Test
    public void verifyFindByName() {
        when(emMock.createQuery(anyString(), eq(Product.class))).thenReturn(queryProductMock);

        when(queryProductMock.getResultList()).thenReturn(products);

        dao.readByName("test");

        verify(emMock).createQuery(anyString(), eq(Product.class));
        verify(queryProductMock).getResultList();
    }

    @Test
    public void verifyCreateQueryIsCalledDuringReadAllProducts() {
        when(emMock.createQuery(anyString(), eq(Product.class))).thenReturn(queryProductMock);

        when(queryProductMock.getResultList()).thenReturn(products);

        dao.readAllProducts();

        verify(emMock).createQuery(anyString(), eq(Product.class));
        verify(queryProductMock).getResultList();
    }
}
