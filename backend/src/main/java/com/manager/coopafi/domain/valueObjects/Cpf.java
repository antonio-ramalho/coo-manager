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
@NoArgsConstructor(access = AccessLevel.PROTECTED,  force = true)
public class Cpf {

    @Column(name = "cpf")
    String cpfNumber;

    public Cpf(String cpfNumber) {
        ifNull(cpfNumber);
        String cleanCpf = cpfNumber.replaceAll("\\D", "");
        validateLength(cleanCpf);
        validateDigits(cleanCpf);
        this.cpfNumber = cleanCpf;
    }

    private void ifNull(String value) {
        if (Objects.isNull(value)) {
            throw new DomainException("O CPF não pode ser nulo.");
        }
    }

    private void validateLength(String value) {
        if (value.length() != 11) {
            throw new DomainException("O CPF deve conter exatamente 11 dígitos.");
        }
    }

    private void validateDigits(String value) {
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += (value.charAt(i) - '0') * (10 - i);
        }
        int digito = 11 - (soma % 11);
        if (digito > 9) digito = 0;

        if (digito != (value.charAt(9) - '0')) {
            throw new DomainException("Dígito verificador do CPF inválido.");
        }
    }
}
