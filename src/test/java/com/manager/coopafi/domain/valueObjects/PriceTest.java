package com.manager.coopafi.domain.valueObjects;

import com.manager.coopafi.exceptions.DomainException;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class PriceTest {

    @Test
    void testValidPrice() {
        Price price = new Price(BigDecimal.valueOf(10.50));
        assertNotNull(price);
        assertEquals(BigDecimal.valueOf(10.50).setScale(2), price.getValue());
    }

    @Test
    void testNullPriceThrowsException() {
        assertThrows(DomainException.class, () -> new Price(null));
    }

    @Test
    void testNegativePriceThrowsException() {
        assertThrows(DomainException.class, () -> new Price(BigDecimal.valueOf(-1.00)));
    }

    @Test
    void testAddPrices() {
        Price p1 = new Price(BigDecimal.valueOf(10.00));
        Price p2 = new Price(BigDecimal.valueOf(5.00));
        Price result = p1.add(p2);
        assertEquals(BigDecimal.valueOf(15.00).setScale(2), result.getValue());
    }

    @Test
    void testAddNullPrice() {
        Price p1 = new Price(BigDecimal.valueOf(10.00));
        Price result = p1.add(null);
        assertEquals(BigDecimal.valueOf(10.00).setScale(2), result.getValue());
    }

    @Test
    void testSubtractPrices() {
        Price p1 = new Price(BigDecimal.valueOf(10.00));
        Price p2 = new Price(BigDecimal.valueOf(5.00));
        Price result = p1.subtract(p2);
        assertEquals(BigDecimal.valueOf(5.00).setScale(2), result.getValue());
    }

    @Test
    void testSubtractNullPrice() {
        Price p1 = new Price(BigDecimal.valueOf(10.00));
        Price result = p1.subtract(null);
        assertEquals(BigDecimal.valueOf(10.00).setScale(2), result.getValue());
    }

    @Test
    void testMultiplyByQuantity() {
        Price p1 = new Price(BigDecimal.valueOf(10.00));
        Price result = p1.multiply(BigDecimal.valueOf(3));
        assertEquals(BigDecimal.valueOf(30.00).setScale(2), result.getValue());
    }

    @Test
    void testMultiplyByZero() {
        Price p1 = new Price(BigDecimal.valueOf(10.00));
        Price result = p1.multiply(BigDecimal.ZERO);
        assertEquals(BigDecimal.valueOf(0.00).setScale(2), result.getValue());
    }

    @Test
    void testMultiplyByNegative() {
        Price p1 = new Price(BigDecimal.valueOf(10.00));
        Price result = p1.multiply(BigDecimal.valueOf(-1));
        assertEquals(BigDecimal.valueOf(0.00).setScale(2), result.getValue());
    }

    @Test
    void testMultiplyByNull() {
        Price p1 = new Price(BigDecimal.valueOf(10.00));
        Price result = p1.multiply(null);
        assertEquals(BigDecimal.valueOf(0.00).setScale(2), result.getValue());
    }
}
