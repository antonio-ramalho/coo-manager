package com.manager.coopafi.domain.valueObjects;

import com.manager.coopafi.exceptions.DomainException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PhoneTest {

    @Test
    void testValidPhone10Digits() {
        Phone phone = new Phone("(11) 1234-5678");
        assertNotNull(phone);
        assertEquals("1112345678", phone.getPhoneNumber());
    }

    @Test
    void testValidPhone11Digits() {
        Phone phone = new Phone("(11) 91234-5678");
        assertNotNull(phone);
        assertEquals("11912345678", phone.getPhoneNumber());
    }

    @Test
    void testNullPhoneThrowsException() {
        assertThrows(NullPointerException.class, () -> new Phone(null));
    }

    @Test
    void testShortPhoneThrowsException() {
        assertThrows(DomainException.class, () -> new Phone("123456789"));
    }

    @Test
    void testLongPhoneThrowsException() {
        assertThrows(DomainException.class, () -> new Phone("123456789012"));
    }
}
