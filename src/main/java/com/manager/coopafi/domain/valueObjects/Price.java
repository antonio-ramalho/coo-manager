package com.manager.coopafi.domain.valueObjects;

import com.manager.coopafi.exceptions.DomainException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Embeddable
@Value
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class Price {

    @Column(name = "price_value", precision = 12, scale = 2)
    BigDecimal value;

    public Price(BigDecimal value) {
        validate(value);
        this.value = value.setScale(2, RoundingMode.HALF_UP);
    }

    public Price add(Price other) {
        if (Objects.isNull(other)) return this;
        return new Price(this.value.add(other.getValue()));
    }

    public Price subtract(Price other) {
        if (Objects.isNull(other)) return this;
        return new Price(this.value.subtract(other.getValue()));
    }

    public Price multiply(BigDecimal quantity) {
        if (Objects.isNull(quantity) || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            return new Price(BigDecimal.ZERO);
        }
        return new Price(this.value.multiply(quantity));
    }

    private void validate(BigDecimal value) {
        if (Objects.isNull(value)) {
            throw new DomainException("O valor do preço não pode ser nulo.");
        }

        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainException("O valor do preço não pode ser negativo.");
        }
    }
}