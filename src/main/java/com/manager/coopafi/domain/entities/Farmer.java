package com.manager.coopafi.domain.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.manager.coopafi.domain.valueObjects.Address;
import com.manager.coopafi.domain.valueObjects.Cpf;
import com.manager.coopafi.domain.valueObjects.Email;
import com.manager.coopafi.domain.valueObjects.Phone;
import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "tb_farmer")
public class Farmer implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Embedded
    private Cpf cpf;
    @Embedded
    private Email email;
    @Embedded
    private Phone phone;
    @Embedded
    private Address address;

    public Farmer() {}

    public Farmer(Long id, Cpf cpf, Address address, Email email, String name, Phone phone) {
        this.id = id;
        this.cpf = cpf;
        this.address = address;
        this.email = email;
        this.name = name;
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Farmer farmer = (Farmer) o;
        return Objects.equals(id, farmer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public Address getAddress() {
        return address;
    }

    public Cpf getCpf() {
        return cpf;
    }

    public Long getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }
}
