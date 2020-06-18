package org.example.resources;

import org.example.dao.ProductDao;
import org.example.domain.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.example.testUtil.Util.giveImageAsByteArray;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductResourceTest {

    private Product product;

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductResource productResource;

    @Before
    public void setup() {
        product = Product.builder()
                .name("name")
                .price(666.66)
                .thumbnail(giveImageAsByteArray())
                .build();
    }

    @Test
    public void daoReadIsCalled() {
        List<Product> productList = new ArrayList<>();
        productList.add(product);

        when(productDao.readAllProducts()).thenReturn(productList);

        assertEquals(productList, productResource.get());

        verify(productDao).readAllProducts();
    }

    @Test
    public void daoReadByNameIsCalled() {
        List<Product> productList = new ArrayList<>();
        productList.add(product);

        when(productDao.readByName(any())).thenReturn(productList);

        assertEquals(productList, productResource.getByName("test"));

        verify(productDao).readByName(any());
    }

    @Test
    public void daoCreateIsCalled() {
        productResource.Post(product);

        verify(productDao).create(any());
    }
}
