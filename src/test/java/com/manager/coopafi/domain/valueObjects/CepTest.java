package com.manager.coopafi.domain.valueObjects;

import com.manager.coopafi.exceptions.DomainException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CepTest {

    @Test
    void testValidCep() {
        Cep cep = new Cep("12345-678");
        assertNotNull(cep);
        assertEquals("12345678", cep.getCepNumber());
    }

    @Test
    void testNullCepThrowsException() {
        assertThrows(DomainException.class, () -> new Cep(null));
    }

    @Test
    void testShortCepThrowsException() {
        assertThrows(DomainException.class, () -> new Cep("1234567"));
    }

    @Test
    void testLongCepThrowsException() {
        assertThrows(DomainException.class, () -> new Cep("123456789"));
    }
}
