package com.manager.coopafi.personModule.entities;

import com.manager.coopafi.infrastructure.valueObjects.*;
import com.manager.coopafi.personModule.enums.Gender;
import com.manager.coopafi.personModule.enums.PersonType;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.Objects;

@Entity
@Table(name = "tb_natural_person")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NaturalPerson extends Person {

    @Embedded
    @Column(unique = true, nullable = false)
    private Cpf cpf;

    @Embedded
    private BirthDate birthDate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    public NaturalPerson(Address address, Email email, Phone phone,
                        String legalName, Cpf cpf, BirthDate birthDate, Gender gender) {
        super(legalName, address, email, phone, PersonType.NATURAL);
        this.cpf = Objects.requireNonNull(cpf);
        this.birthDate = Objects.requireNonNull(birthDate);
        this.gender = Objects.requireNonNull(gender);
    }

    public void updateGender(Gender gender) {
        this.gender = Objects.requireNonNull(gender);
    }

    public void updateBirthDate(BirthDate birthDate) {
        this.birthDate = Objects.requireNonNull(birthDate);
    }

    @Override
    public String getDocumentNumber() {
        return this.cpf != null ? this.cpf.getCpfNumber() : null;
    }
}
