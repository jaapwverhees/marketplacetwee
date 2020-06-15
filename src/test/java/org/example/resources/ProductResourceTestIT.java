package org.example.resources;

import org.example.App;
import org.example.dao.ProductDao;
import org.example.domain.Product;
import org.example.util.ContainerFilter;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;

import static javax.ws.rs.client.Entity.json;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

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
    public void getAllProducts() {
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

    @Test
    public void getByName(){
        Product productOne = Product.builder()
                .name("product")
                .price(222.22)
                .build();
        Product productTwo = Product.builder()
                .name("toyota")
                .price(222.22)
                .build();
        Product productThree = Product.builder()
                .name("viaduct")
                .price(222.22)
                .build();

        dao.create(productOne);
        dao.create(productTwo);
        dao.create(productThree);
        String message = ClientBuilder.newClient()
                .target(productResource + "/duct")
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);

        assertFalse(message.contains("toyota"));
        assertThat(message, containsString("product"));
        assertThat(message, containsString("viaduct"));


    }

    @Test
    public void PostProduct() {

        Product productOne = Product.builder()
                .name("postTest")
                .price(999.99)
                .thumbnail(giveImageAsByteArray())
                .build();

        ClientBuilder.newClient()
                .target(productResource)
                .request(APPLICATION_JSON)
                .post(json(productOne), Product.class);

        Product productTwo = findProductFromArray(dao.readAllProducts(), "postTest");

        assertNotNull(productTwo);

    }

    private Product findProductFromArray(List<Product> list, String parameter) {
        for (Product product : list) {
            if (product.getName().equals(parameter)) {
                return product;
            }
        }
        return null;
    }

    private byte[] giveImageAsByteArray() {
        File file = new File("C:\\Users\\jaapw\\Desktop\\markplace 2.0\\images\\image.jpg");
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            System.out.println("oops");
        }
        return null;
    }
}
