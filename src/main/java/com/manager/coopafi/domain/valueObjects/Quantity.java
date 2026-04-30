package com.manager.coopafi.domain.valueObjects;

import com.manager.coopafi.exceptions.DomainException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.jspecify.annotations.NonNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Embeddable
@Value
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class Quantity implements Comparable<Quantity> {

    @Column(name = "quantity_amount", precision = 12, scale = 3)
    BigDecimal amount;

    public Quantity(BigDecimal amount) {
        validate(amount);
        this.amount = amount.setScale(3, RoundingMode.HALF_UP);
    }

    public Quantity add(Quantity other) {
        if (Objects.isNull(other)) return this;
        return new Quantity(this.amount.add(other.getAmount()));
    }

    public Quantity subtract(Quantity other) {
        if (Objects.isNull(other)) return this;

        if (this.amount.compareTo(other.getAmount()) < 0) {
            throw new DomainException("Operação inválida: A quantidade a ser subtraída é maior que a quantidade atual.");
        }

        return new Quantity(this.amount.subtract(other.getAmount()));
    }

    private void validate(BigDecimal amount) {
        if (Objects.isNull(amount)) {
            throw new DomainException("A quantidade não pode ser nula.");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainException("A quantidade não pode ser negativa.");
        }
    }

    @Override
    public int compareTo(@NonNull Quantity other) {
        return this.amount.compareTo(other.getAmount());
    }
}
