package com.manager.coopafi.domain.valueObjects;

import com.manager.coopafi.exceptions.DomainException;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class BirthDateTest {

    @Test
    void testValidBirthDate() {
        LocalDate date = LocalDate.of(1990, 1, 1);
        BirthDate birthDate = new BirthDate(date);
        assertNotNull(birthDate);
        assertEquals(date, birthDate.getDate());
    }

    @Test
    void testNullDateThrowsException() {
        assertThrows(DomainException.class, () -> new BirthDate(null));
    }

    @Test
    void testFutureDateThrowsException() {
        LocalDate future = LocalDate.now().plusDays(1);
        assertThrows(DomainException.class, () -> new BirthDate(future));
    }

    @Test
    void testUnderageThrowsException() {
        LocalDate underage = LocalDate.now().minusYears(17);
        assertThrows(DomainException.class, () -> new BirthDate(underage));
    }
}
