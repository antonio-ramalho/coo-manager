package com.manager.coopafi.domain.valueObjects;

import com.manager.coopafi.exceptions.DomainException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CafNumberTest {

    @Test
    void testValidCafNumber() {
        CafNumber caf = new CafNumber("ABC123-DEF");
        assertNotNull(caf);
        assertEquals("ABC123-DEF", caf.getValue());
    }

    @Test
    void testNullValueThrowsException() {
        assertThrows(DomainException.class, () -> new CafNumber(null));
    }

    @Test
    void testEmptyValueThrowsException() {
        assertThrows(DomainException.class, () -> new CafNumber(""));
    }

    @Test
    void testInvalidCharactersThrowsException() {
        assertThrows(DomainException.class, () -> new CafNumber("ABC@123"));
    }
}
