package com.manager.coopafi.domain.valueObjects;

import com.manager.coopafi.exceptions.DomainException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Test
    void testValidEmail() {
        Email email = new Email("test@example.com");
        assertNotNull(email);
        assertEquals("test@example.com", email.getAddress());
    }

    @Test
    void testNullEmailThrowsException() {
        assertThrows(DomainException.class, () -> new Email(null));
    }

    @Test
    void testEmailWithoutAtThrowsException() {
        assertThrows(DomainException.class, () -> new Email("testexample.com"));
    }
}
