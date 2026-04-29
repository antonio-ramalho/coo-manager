package com.manager.coopafi.domain.valueObjects;

import com.manager.coopafi.exceptions.DomainException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class AddressTest {

    @Test
    void testValidAddress() {
        Cep cep = mock(Cep.class);
        Address address = new Address(cep, "Rua A", "Bairro B", "Cidade C", "123");
        assertNotNull(address);
        assertEquals("Rua A", address.getStreet());
    }

    @Test
    void testNullCepThrowsException() {
        assertThrows(NullPointerException.class, () -> new Address(null, "Rua A", "Bairro B", "Cidade C", "123"));
    }

    @Test
    void testNullStreetThrowsException() {
        Cep cep = mock(Cep.class);
        assertThrows(DomainException.class, () -> new Address(cep, null, "Bairro B", "Cidade C", "123"));
    }

    @Test
    void testEmptyStreetThrowsException() {
        Cep cep = mock(Cep.class);
        assertThrows(DomainException.class, () -> new Address(cep, "", "Bairro B", "Cidade C", "123"));
    }

    @Test
    void testNullNeighborhoodThrowsException() {
        Cep cep = mock(Cep.class);
        assertThrows(DomainException.class, () -> new Address(cep, "Rua A", null, "Cidade C", "123"));
    }

    @Test
    void testNullCityThrowsException() {
        Cep cep = mock(Cep.class);
        assertThrows(DomainException.class, () -> new Address(cep, "Rua A", "Bairro B", null, "123"));
    }

    @Test
    void testNullNumberThrowsException() {
        Cep cep = mock(Cep.class);
        assertThrows(DomainException.class, () -> new Address(cep, "Rua A", "Bairro B", "Cidade C", null));
    }
}
