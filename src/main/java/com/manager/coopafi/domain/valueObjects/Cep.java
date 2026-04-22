package com.manager.coopafi.domain.valueObjects;

import com.manager.coopafi.exceptions.DomainException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public final class Cep {

    @Column(name = "cep")
    private final String cepNumber;

    protected Cep() { this.cepNumber = null; }

    public Cep(String cep) {
        validate(cep);
        this.cepNumber = cep.replaceAll("\\D", "");
    }

    private void validate(String value) {
        if (Objects.isNull(value) || value.replaceAll("\\D", "").length() != 8) {
            throw new DomainException("CEP inválido. Deve conter 8 dígitos.");
        }
    }

    public String getCepNumber() {
        return cepNumber;
    }
}
