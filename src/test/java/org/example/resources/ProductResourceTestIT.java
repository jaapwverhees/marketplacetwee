package org.example.resources;

import org.example.App;
import org.example.dao.ProductDao;
import org.example.dao.VisitorDao;
import org.example.domain.Product;
import org.example.domain.Visitor;
import org.example.util.ContainerFilter;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import javax.inject.Inject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.net.URL;
import java.util.List;

import static javax.ws.rs.client.Entity.json;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(Arquillian.class)
public class ProductResourceTestIT {
    @ArquillianResource
    private URL deploymentURL;

    private String productResource;

    @Inject
    private ProductDao dao;


    @Deployment
    public static Archive<?> createDeployment() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, "test.war")
                .addClass(App.class) // dont forget!
                .addClass(Product.class)
                .addClass(ProductDao.class)
                .addClass(ProductResource.class)
                .addClass(ContainerFilter.class)
                .addClass(ContainerResponseFilter.class)
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
        productResource = deploymentURL + "resources/products";
    }

    @Test
    public void getAllProducts(){
        Product productOne = Product.builder()
                .name("testProduct")
                .price(222.22)
                .build();
        Product productTwo = Product.builder()
                .name("testProduct")
                .price(222.22)
                .build();

        dao.create(productOne);
        dao.create(productTwo);
        String message = ClientBuilder.newClient()
                .target(productResource)
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);

        assertThat(message, containsString("[{\"id\":1,\"name\":\"testProduct\",\"price\":222.22},{\"id\":2,\"name\":\"testProduct\",\"price\":222.22}]"));

    }

    //FIXME for the time being without thumbnail;
    @Test
    public void PostProduct() {

        Product productOne = Product.builder()
                .name("postTest")
                .price(999.99)
                .build();

        ClientBuilder.newClient()
                .target(productResource)
                .request(APPLICATION_JSON)
                .post(json(productOne), Product.class);

        Product productTwo = findProductByName(dao.readAllProducts(), "postTest");

        assertNotNull(productTwo);

    }
    private Product findProductByName(List<Product> list, String parameter){
        for (Product product: list) {
            if (product.getName().equals(parameter)){
                return product;
            }
        }
        return null;
    }
}
