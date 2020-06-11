package org.example.resources;

import junit.framework.TestCase;
import org.example.App;
import org.example.dao.StudentDao;
import org.example.dao.VisitorDao;
import org.example.domain.Student;
import org.example.domain.Visitor;
import org.example.services.StudentService;
import org.hamcrest.CoreMatchers;
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
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.net.URL;

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
    public void testGetVisitor() {
        visitorDao.create(Visitor.builder()
                .emailadress("test@test.nl")
                .lastname("lastname")
                .firstname("firstname")
                .password("thisIsAPassword")
                .build());
        assertEquals("firstname", visitorDao.read("test@test.nl").getFirstname());
        String message = ClientBuilder.newClient()
                .target(visitorResource)
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);

        assertThat(message, containsString("{\"emailadress\":\"test@test.nl\",\"firstname\":\"firstname\",\"lastname\":\"lastname\",\"password\":\"thisIsAPassword\"}"));
    }
}
