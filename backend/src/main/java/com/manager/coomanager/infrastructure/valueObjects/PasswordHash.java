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
public class PasswordHash {

    @Column(name = "password_hash")
    String hash;

    public PasswordHash(String hash) {
        validate(hash);
        this.hash = hash;
    }

    private void validate(String value) {
        if (Objects.isNull(value) || value.trim().isBlank()) {
            throw new DomainException("O hash da senha não pode ser nulo ou vazio.");
        }
        if (value.trim().length() < 8) {
            throw new DomainException("A senha fornecida é muito curta para ser segura.");
        }
    }
}
