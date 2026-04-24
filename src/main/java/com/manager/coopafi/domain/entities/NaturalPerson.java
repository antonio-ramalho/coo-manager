package com.manager.coopafi.domain.entities;

import com.manager.coopafi.domain.valueObjects.*;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "tb_natural_person")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NaturalPerson extends Person {

    private String name;
    @Embedded
    private Cpf cpf;

    public NaturalPerson(Address address, Email email, Phone phone,
                         String name, Cpf cpf, BirthDate birthDate) {
        super(address, email, phone, birthDate);
        this.name = name;
        this.cpf = cpf;
    }

    public void updateName(String newName) {
        this.name = Objects.requireNonNull(newName);
    }
}
