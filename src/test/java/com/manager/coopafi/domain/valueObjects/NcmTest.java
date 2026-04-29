package com.manager.coopafi.domain.valueObjects;

import com.manager.coopafi.exceptions.DomainException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NcmTest {

    @Test
    void testValidNcm() {
        Ncm ncm = new Ncm("12345678");
        assertNotNull(ncm);
        assertEquals("12345678", ncm.getCode());
    }

    @Test
    void testNullNcmThrowsException() {
        assertThrows(DomainException.class, () -> new Ncm(null));
    }

    @Test
    void testShortNcmThrowsException() {
        assertThrows(DomainException.class, () -> new Ncm("1234567"));
    }

    @Test
    void testLongNcmThrowsException() {
        assertThrows(DomainException.class, () -> new Ncm("123456789"));
    }

    @Test
    void testNonNumericNcmThrowsException() {
        assertThrows(DomainException.class, () -> new Ncm("1234567a"));
    }
}
