package com.manager.coopafi.domain.valueObjects;

import com.manager.coopafi.exceptions.DomainException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Objects;

@Embeddable
@Value
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class Address {

    @Embedded
    Cep cep;
    @Column(name = "street")
    String street;
    @Column(name = "neighborhood")
    String neighborhood;
    @Column(name = "city")
    String city;
    @Column(name = "number")
    String addressNumber;

    public Address(Cep cep, String street, String neighborhood, String city, String number) {
        validateInput(street, neighborhood, city, number);
        this.cep = Objects.requireNonNull(cep, "CEP é obrigatório.");
        this.street = street;
        this.neighborhood = neighborhood;
        this.city = city;
        this.addressNumber = number;
    }

    private void validateInput(String street, String neighborhood, String city, String number) {
        validateTxt(street, "Logradouro");
        validateTxt(neighborhood, "Bairro");
        validateTxt(city, "Cidade");
        validateTxt(number, "Número");
    }

    private void validateTxt(String value, String inputName) {
        if (Objects.isNull(value) || value.trim().isEmpty()) {
            throw new DomainException(inputName + " não pode estar vazio.");
        }
    }
}
