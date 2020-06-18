package org.example.resources;

import org.example.dao.VisitorDao;
import org.example.domain.Visitor;
import org.example.util.PasswordAuthentication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class VisitorResourceTest {

    private Visitor visitor;

    @Mock
    private VisitorDao visitorDao;


    private final PasswordAuthentication auth = new PasswordAuthentication();

    @Mock
    private PasswordAuthentication authMock;

    @InjectMocks
    private VisitorResource visitorResource;

    @Before
    public void setup() {
        visitor = Visitor.builder()
                .emailadress("email@email.com")
                .firstname("firstname")
                .lastname("lastname")
                .password(auth.hash("ThisIsAPassword1!"))
                .build();
    }

    @Test
    public void VerifyVisitorDotReadIsCalled() throws Exception {
        when(visitorDao.read(any())).thenReturn(visitor);
        when(authMock.authenticate(any(), any())).thenReturn(true);

        visitorResource.Login("IDString", "ThisIsAPassword1!");

        verify(visitorDao).read("IDString");
    }

    @Test
    public void validLogin() throws Exception {
        when(visitorDao.read(any())).thenReturn(visitor);

        when(authMock.authenticate(any(), any())).thenReturn(true);

        visitorResource.Login("IDString", "ThisIsAPassword1!");

        assertEquals(visitorDao.read("IDString"), visitor);
    }

    @Test
    public void InvalidLoginDoesNotExist() {
        when(visitorDao.read(any())).thenReturn(null);

        Exception exception = assertThrows(Exception.class, () ->
                visitorResource.Login("IDString", "ThisIsAPassword1!"));

        assertEquals(exception.getMessage(), "Visitor does Not Exists");
    }

    @Test
    public void InvalidLoginInvalidPassword() {
        when(visitorDao.read(any())).thenReturn(visitor);

        when(authMock.authenticate(any(), any())).thenReturn(false);

        Exception exception = assertThrows(Exception.class, () ->
                visitorResource.Login("IDString", "ThisIsAInvalidPassword1!"));

        assertEquals(exception.getMessage(), "Password is invalid");
    }

    @Test
    public void postTestDaoCreateIsCalled() {
        when(authMock.hash(any())).thenReturn("hashedString");

        visitorResource.Post(visitor);

        verify(visitorDao).create(any());
    }

}
