package com.manager.coopafi.domain.valueObjects;

import com.manager.coopafi.exceptions.DomainException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Objects;

@Embeddable
@Value
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class CafNumber {

    @Column(name = "ricaf_number")
    String value;

    public CafNumber(String value) {
        validate(value);
        this.value = value.toUpperCase().trim();
    }

    private void validate(String value) {
        if (Objects.isNull(value) || value.trim().isEmpty()) {
            throw new DomainException("O número do RICAF é obrigatório.");
        }

        if (!value.matches("^[A-Z0-9-]+$")) {
            throw new DomainException("Formato de RICAF inválido.");
        }
    }
}