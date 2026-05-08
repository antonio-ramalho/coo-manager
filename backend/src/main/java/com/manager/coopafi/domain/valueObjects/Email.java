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
public class Email {

    @Column(name = "addressEmail")
    String addressEmail;

    public Email(String adress) {
        validate(adress);
        this.addressEmail = adress.toLowerCase();
    }

    private void validate(String valor) {
        if (Objects.isNull(valor) || !valor.contains("@")) {
            throw new DomainException("E-mail inválido.");
        }
    }
}
