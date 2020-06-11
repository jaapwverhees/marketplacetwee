package org.example.resources;

import org.example.App;
import org.example.dao.StudentDao;
import org.example.domain.Student;
import org.example.domain.Values;
import org.example.services.StudentService;
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

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.net.URL;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Arquillian.class)
public class StudentsResourceIT {

    @ArquillianResource
    private URL deploymentURL;

    private String studentsResource;

    @Before
    public void setup() {
        studentsResource = deploymentURL + "resources/students";
    }

    @Deployment
    public static Archive<?> createDeployment() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, "test.war")
                .addClass(App.class) // dont forget!
                .addClass(StudentsResource.class)
                .addClass(StudentService.class)
                .addClass(StudentDao.class)
                .addPackage(Student.class.getPackage())
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

    @Test
    public void getStudentsReturnsStudents() {
        String message = ClientBuilder.newClient()
                .target(studentsResource)
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);

        assertThat(message, containsString("{\"students\":["));
    }

    @Test
    public void postStudentAddsStudentToResource() {
        Student response = ClientBuilder.newClient()
                .target(studentsResource)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(Values.JANSSENS), Student.class);

        assertThat(response.getId(), is(not(nullValue())));
    }

}
