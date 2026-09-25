package com.manager.coopafi.infrastructure.valueObjects;

import com.manager.coopafi.infrastructure.exceptions.DomainException;
import com.manager.coopafi.personModule.enums.BrazilianState;
import jakarta.persistence.*;
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
    @Column(name = "addressNumber")
    String addressNumber;
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    BrazilianState state;

    public Address(Cep cep, String street, String neighborhood, String city, String number, BrazilianState state) {
        validateInput(street, neighborhood, city);
        this.cep = Objects.requireNonNull(cep, "CEP é obrigatório.");
        this.street = street;
        this.neighborhood = neighborhood;
        this.city = city;
        this.addressNumber = validateAddressNumber(number);
        this.state = state;
    }

    private void validateInput(String street, String neighborhood, String city) {
        validateTxt(street, "Logradouro");
        validateTxt(neighborhood, "Bairro");
        validateTxt(city, "Cidade");
    }

    private String validateAddressNumber(String number) {
        if (number == null || number.isEmpty()) {
            return "SN";
        }
        return number;
    }

    private void validateTxt(String value, String inputName) {
        if (Objects.isNull(value) || value.trim().isEmpty()) {
            throw new DomainException(inputName + " não pode estar vazio.");
        }
    }
}
