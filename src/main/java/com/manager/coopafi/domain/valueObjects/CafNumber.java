package com.manager.coopafi.domain.valueObjects;

import com.manager.coopafi.exceptions.DomainException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public final class CafNumber {

    @Column(name = "ricaf_number")
    private final String value;

    protected CafNumber() { this.value = null; }

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

    public String getValue() { return value; }
}