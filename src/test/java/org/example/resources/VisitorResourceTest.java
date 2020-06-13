package org.example.resources;

import org.example.dao.VisitorDao;
import org.example.domain.Visitor;
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

    Visitor visitor;

    @Mock
    private VisitorDao visitorDao;

    @InjectMocks
    private VisitorResource visitorResource;

    @Before
    public void setup() {
        visitor = Visitor.builder()
                .emailadress("email@email.com")
                .firstname("firstname")
                .lastname("lastname")
                .password("ThisIsAPassword")
                .build();
    }

    @Test
    public void VerifyVisitorDotReadIsCalled() throws Exception {
        when(visitorDao.read(any())).thenReturn(visitor);

        visitorResource.Login("IDString", "ThisIsAPassword");

        verify(visitorDao).read("IDString");
    }

    @Test
    public void validLogin() throws Exception {
        when(visitorDao.read(any())).thenReturn(visitor);

        visitorResource.Login("IDString", "ThisIsAPassword");

        assertEquals(visitorDao.read("IDString"), visitor);
    }

    @Test
    public void InvalidLoginDoesNotExist() {
        when(visitorDao.read(any())).thenReturn(null);

        Exception exception = assertThrows(Exception.class, () ->
                visitorResource.Login("IDString", "ThisIsAPassword"));

        assertEquals(exception.getMessage(), "Visitor does Not Exists");
    }

    @Test
    public void InvalidLoginInvalidPassword() {
        when(visitorDao.read(any())).thenReturn(visitor);

        Exception exception = assertThrows(Exception.class, () ->
                visitorResource.Login("IDString", "ThisIsAInvalidPassword"));

        assertEquals(exception.getMessage(), "Password is invalid");
    }

    @Test
    public void postTestDaoCreateIsCalled(){

        visitorResource.Post(Visitor.builder().build());
        verify(visitorDao).create(any());
    }

}
