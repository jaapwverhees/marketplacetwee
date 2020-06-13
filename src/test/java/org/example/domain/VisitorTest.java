package org.example.domain;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class VisitorTest {
    Visitor visitor;

    @Before
    public void setup() {
        visitor = Visitor.builder()
                .firstname("firstname")
                .lastname("lastname")
                .emailadress("valdidEmail@email.com")
                .password("ThisIsAValidPassWord")
                .build();
    }

    @Test
    public void validMethodReturnsFalseForInvalidEmail() {
        visitor.setEmailadress(null);
        assertFalse(visitor.valid());

        visitor.setEmailadress("invalid");
        assertFalse(visitor.valid());

        visitor.setEmailadress("invalid@invald");
        assertFalse(visitor.valid());

        visitor.setEmailadress("invalid@.com");
        assertFalse(visitor.valid());

        visitor.setEmailadress("@invalid.com");
        assertFalse(visitor.valid());

        visitor.setEmailadress("invalid.com");
        assertFalse(visitor.valid());
    }

    @Test
    public void validMethodReturnsTrueForValidEmail() {
        visitor.setEmailadress("valid@valid.com");
        assertTrue(visitor.valid());
    }

    @Test
    public void validMethodReturnsFalseForInvalidFirstName() {
        visitor.setFirstname(null);
        assertFalse(visitor.valid());

        visitor.setFirstname("I");
        assertFalse(visitor.valid());

        visitor.setFirstname("firstNameIsLongerThenTwentyCharacters");
        assertFalse(visitor.valid());
    }

    @Test
    public void validMethodReturnsFalseForInvalidLastName() {
        visitor.setLastname(null);
        assertFalse(visitor.valid());

        visitor.setLastname("I");
        assertFalse(visitor.valid());

        visitor.setLastname("lastNameIsLongerThenTwentyCharacters");
        assertFalse(visitor.valid());
    }

    @Test
    public void validMethodReturnsTrueFoValidLastName() {
        visitor.setLastname("smith");
        assertTrue(visitor.valid());
    }

    @Test
    public void validMethodReturnsTrueForValidPassword() {
        visitor.setPassword("thisIsAValidPassword");
        assertTrue(visitor.valid());
    }

    @Test
    public void validMethodReturnsFalseForInvalidPassword() {
        visitor.setPassword("thisIsAValidPassword");
        assertTrue(visitor.valid());
    }

}
