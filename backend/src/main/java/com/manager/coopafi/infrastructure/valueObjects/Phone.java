package com.manager.coopafi.infrastructure.valueObjects;

import com.manager.coopafi.infrastructure.exceptions.DomainException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Objects;

@Embeddable
@Value
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class Phone {

    @Column(name = "Phone", length = 11)
    String phoneNumber;

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
}