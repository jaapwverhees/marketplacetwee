package org.example.resources;

import junit.framework.TestCase;
import org.example.App;
import org.example.util.ContainerFilter;
import org.example.dao.VisitorDao;
import org.example.domain.Visitor;
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
import java.net.URL;

import static javax.ws.rs.client.Entity.json;
import static javax.ws.rs.core.MediaType.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;


@RunWith(Arquillian.class)
public class VisitorResourceTestIT extends TestCase {

    @ArquillianResource
    private URL deploymentURL;

    private String visitorResource;

    @Inject
    private VisitorDao visitorDao;

    @Deployment
    public static Archive<?> createDeployment() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, "test.war")
                .addClass(App.class) // dont forget!
                .addClass(Visitor.class)
                .addClass(VisitorDao.class)
                .addClass(VisitorResource.class)
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
        visitorResource = deploymentURL + "resources/visitor";
    }

    @Test
    public void getVisitor(){
        Visitor visitor = Visitor.builder()
                .emailadress("getVisitor@email.com")
                .firstname("firstname")
                .lastname("lastname")
                .password("thisIsAPassword")
                .build();
        visitorDao.create(visitor);
        String message = ClientBuilder.newClient()
                .target(visitorResource)
                .queryParam("email", "getVisitor@email.com")
                .queryParam("password", "thisIsAPassword")
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);

        assertThat(message, containsString("{\"emailadress\":\"getVisitor@email.com\",\"firstname\":\"firstname\",\"lastname\":\"lastname\",\"password\":\"thisIsAPassword\"}"));

    }

    @Test
    public void PostVisitor() {
        Visitor visitor = Visitor.builder()
                .emailadress("testPostVisitor@email.com")
                .firstname("firstname")
                .lastname("lastname")
                .password("thisIsAPassword")
                .build();
        assertNull(visitorDao.read("testPostVisitor@email.com"));
        ClientBuilder.newClient()
                .target(visitorResource)
                .request(APPLICATION_JSON)
                .post(json(visitor), Visitor.class);
        assertEquals(visitor, visitorDao.read("testPostVisitor@email.com"));
    }
}
