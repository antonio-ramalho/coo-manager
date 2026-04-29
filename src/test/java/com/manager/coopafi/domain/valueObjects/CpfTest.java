package com.manager.coopafi.domain.valueObjects;

import com.manager.coopafi.exceptions.DomainException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CpfTest {

    @Test
    void testValidCpf() {
        Cpf cpf = new Cpf("529.982.247-25");
        assertNotNull(cpf);
        assertEquals("52998224725", cpf.getCpfNumber());
    }

    @Test
    void testNullCpfThrowsException() {
        assertThrows(DomainException.class, () -> new Cpf(null));
    }

    @Test
    void testShortCpfThrowsException() {
        assertThrows(DomainException.class, () -> new Cpf("1234567890"));
    }

    @Test
    void testLongCpfThrowsException() {
        assertThrows(DomainException.class, () -> new Cpf("123456789012"));
    }
}
