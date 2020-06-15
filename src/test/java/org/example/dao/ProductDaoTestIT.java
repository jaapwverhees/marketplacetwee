package org.example.dao;

import org.example.App;
import org.example.domain.Product;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Arquillian.class)
public class ProductDaoTestIT {

    @Inject
    private ProductDao dao;

    private Product productOne;
    private Product productTwo;

    @Deployment
    public static Archive<?> createDeployment() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, "test.war")
                .addClass(App.class) // dont forget!
                .addClass(Product.class)
                .addClass(ProductDao.class)
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsLibraries(hibernate());

        System.out.println(archive.toString(true));
        return archive;
    }

    private static File[] hibernate() {
        return Maven.resolver()
                .loadPomFromFile("pom.xml")
                .resolve("org.hibernate:hibernate-entitymanager")
                .withTransitivity()
                .asFile();
    }

    @Before
    public void setup() {
        productOne = Product.builder()
                .name("testProduct")
                .price(222.22)
                .build();
        productTwo = Product.builder()
                .name("testProduct")
                .price(222.22)
                .build();

    }

    @Test
    public void TestCreateAndReadAllProducts() {
        dao.create(productOne);
        dao.create(productTwo);
        List<Product> products = dao.readAllProducts();
        assertNotNull(products);
        assertEquals(products.size(), 2);
    }
}
