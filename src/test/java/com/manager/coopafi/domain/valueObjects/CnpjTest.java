package com.manager.coopafi.domain.valueObjects;

import com.manager.coopafi.exceptions.DomainException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CnpjTest {

    @Test
    void testValidCnpj() {
        Cnpj cnpj = new Cnpj("12.345.678/0001-90");
        assertNotNull(cnpj);
        assertEquals("12345678000190", cnpj.getCnpjNumber());
    }

    @Test
    void testNullCnpjThrowsException() {
        assertThrows(DomainException.class, () -> new Cnpj(null));
    }

    @Test
    void testShortCnpjThrowsException() {
        assertThrows(DomainException.class, () -> new Cnpj("1234567890123"));
    }

    @Test
    void testLongCnpjThrowsException() {
        assertThrows(DomainException.class, () -> new Cnpj("123456789012345"));
    }
}
