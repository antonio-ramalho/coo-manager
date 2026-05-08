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
public class Ncm {

    @Column(name = "ncm_code")
    String code;

    public Ncm(String code) {
        validate(code);
        this.code = code;
    }

    private void validate(String code) {
        if (Objects.isNull(code) || !code.matches("\\d{8}")) {
            throw new DomainException("O código NCM deve conter exatamente 8 dígitos numéricos.");
        }
    }
}
