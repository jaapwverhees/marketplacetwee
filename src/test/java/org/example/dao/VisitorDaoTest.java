package org.example.dao;

import org.example.domain.Visitor;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class VisitorDaoTest {
    @Mock
    private EntityManager emMock;


    @InjectMocks
    private VisitorDao dao;

    @Test
    public void verifyEntityManagerPersist() {
        doNothing().when(emMock).persist(any());

        dao.create(Visitor.builder().build());

        verify(emMock).persist(any());
    }

    @Test
    public void verifyEntityManagerFind() {
        when(emMock.find(any(), any())).thenReturn(new Visitor());

        dao.read("string");

        verify(emMock).find(any(), any());
    }
}
