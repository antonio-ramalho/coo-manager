package com.manager.coomanager.infrastructure.valueObjects;

import com.manager.coomanager.infrastructure.exceptions.DomainException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Objects;

@Embeddable
@Value
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class StateRegistration {

    @Column(name = "state_registration", length = 20)
    String registrationNumber;

    public StateRegistration(String registration) {
        validate(registration);
        String cleaned = registration.trim().toUpperCase();
        if ("ISENTO".equals(cleaned)) {
            this.registrationNumber = "ISENTO";
        } else {
            this.registrationNumber = cleaned.replaceAll("\\D", "");
        }
    }

    private void validate(String value) {
        if (Objects.isNull(value) || value.trim().isBlank()) {
            throw new DomainException("A Inscrição Estadual não pode ser nula ou vazia.");
        }
        String cleaned = value.trim().toUpperCase();
        if (!"ISENTO".equals(cleaned)) {
            String digitsOnly = cleaned.replaceAll("\\D", "");
            if (digitsOnly.length() < 8 || digitsOnly.length() > 14) {
                throw new DomainException("A Inscrição Estadual numérica deve conter entre 8 e 14 dígitos.");
            }
        }
    }
}
