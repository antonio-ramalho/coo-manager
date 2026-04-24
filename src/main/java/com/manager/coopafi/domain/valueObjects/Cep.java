package com.manager.coopafi.domain.valueObjects;

import com.manager.coopafi.exceptions.DomainException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Objects;

@Embeddable
@Value
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class Cep {

    @Column(name = "cep")
    String cepNumber;

    public Cep(String cep) {
        validate(cep);
        this.cepNumber = cep.replaceAll("\\D", "");
    }

    private void validate(String value) {
        if (Objects.isNull(value) || value.replaceAll("\\D", "").length() != 8) {
            throw new DomainException("CEP inválido. Deve conter 8 dígitos.");
        }
    }
}
