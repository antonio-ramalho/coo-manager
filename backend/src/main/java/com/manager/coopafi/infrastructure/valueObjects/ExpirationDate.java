package com.manager.coopafi.infrastructure.valueObjects;

import com.manager.coopafi.infrastructure.exceptions.DomainException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Embeddable
@Value
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class ExpirationDate {

    @Column(name = "expiration_date")
    LocalDate value;

    public ExpirationDate(LocalDate value) {
        validate(value);
        this.value = value;
    }

    private void validate(LocalDate value) {
        if (value == null) {
            throw new DomainException("A data de validade não pode ser nula.");
        }

        if (value.isBefore(LocalDate.now())) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            throw new DomainException("A data de validade (" + value.format(formatter) + ") não pode ser anterior à data atual.");
        }
    }

    public boolean isExpired() {
        return this.value.isBefore(LocalDate.now());
    }

    public boolean isValid() {
        return this.value.isAfter(LocalDate.now());
    }
}