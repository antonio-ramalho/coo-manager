package com.manager.coopafi.domain.valueObjects;

import com.manager.coopafi.exceptions.DomainException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public final class Phone {

    @Column(name = "Phone")
    private final String phoneNumber;

    protected Phone() { this.phoneNumber = null; }

    public Phone(String phoneNumber) {
        String cleanNumber = Objects.requireNonNull(phoneNumber, "Telefone é obrigatório")
                .replaceAll("\\D", "");
        validate(cleanNumber);
        this.phoneNumber = cleanNumber;
    }

    private void validate(String valor) {
        if (valor.length() < 10 || valor.length() > 11) {
            throw new DomainException("Telefone deve ter 10 ou 11 dígitos (com DDD).");
        }
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}