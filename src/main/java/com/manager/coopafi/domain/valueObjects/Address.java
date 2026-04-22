package com.manager.coopafi.domain.valueObjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.manager.coopafi.exceptions.DomainException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

import java.util.Objects;

@Embeddable
public final class Address {

    @Embedded
    private final Cep cep;
    @Column(name = "street")
    private final String street;
    @Column(name = "neighborhood")
    private final String neighborhood;
    @Column(name = "city")
    private final String city;
    @Column(name = "number")
    @JsonProperty("Number")
    private final String addressNumber;

    protected Address() {
        this.cep = null;
        this.street = null;
        this.neighborhood = null;
        this.city = null;
        this.addressNumber = null;
    }

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

    public String getAddressNumber() {
        return addressNumber;
    }

    public String getCity() {
        return city;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getStreet() {
        return street;
    }
}
