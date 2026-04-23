package com.manager.coopafi.domain.entities;

import com.manager.coopafi.domain.valueObjects.*;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_natural_person")
public class NaturalPerson extends Person {

    private String name;
    @Embedded
    private Cpf cpf;

    protected NaturalPerson() {}

    public NaturalPerson(Address address, Email email, Phone phone,
                         String name, Cpf cpf, BirthDate birthDate) {
        super(address, email, phone, birthDate);
        this.name = name;
        this.cpf = cpf;
    }

    public Cpf getCpf() {
        return cpf;
    }

    public String getName() {
        return name;
    }
}
