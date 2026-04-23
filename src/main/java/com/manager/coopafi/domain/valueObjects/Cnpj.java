package com.manager.coopafi.domain.valueObjects;

import com.manager.coopafi.exceptions.DomainException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public final class Cnpj {

    @Column(name = "cnpj")
    private final String cnpjNumber;

    protected Cnpj() { this.cnpjNumber = null; }

    public Cnpj(String cnpj) {
        ifNull(cnpj);
        String cleanCnpj = cnpj.replaceAll("\\D", "");
        validateLength(cleanCnpj);
        this.cnpjNumber = cleanCnpj;
    }

    private void ifNull(String value) {
        if (Objects.isNull(value)) {
            throw new DomainException("O CNPJ não pode ser nulo.");
        }
    }

    private void validateLength(String value) {
        if (value.length() != 14) {
            throw new DomainException("O CNPJ deve conter exatamente 14 dígitos.");
        }
    }

    public String getCnpjNumber() { return cnpjNumber; }
}
