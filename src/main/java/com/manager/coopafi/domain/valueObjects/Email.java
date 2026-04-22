package com.manager.coopafi.domain.valueObjects;

import com.manager.coopafi.exceptions.DomainException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public final class Email {

    @Column(name = "email")
    private final String address;
    protected Email() { this.address = null; }

    public Email(String adress) {
        validate(adress);
        this.address = adress.toLowerCase();
    }

    private void validate(String valor) {
        if (Objects.isNull(valor) || !valor.contains("@")) {
            throw new DomainException("E-mail inválido.");
        }
    }

    public String getAddress() {
        return address;
    }
}
