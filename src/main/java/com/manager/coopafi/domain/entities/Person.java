package com.manager.coopafi.domain.entities;

import com.manager.coopafi.domain.valueObjects.Address;
import com.manager.coopafi.domain.valueObjects.BirthDate;
import com.manager.coopafi.domain.valueObjects.Email;
import com.manager.coopafi.domain.valueObjects.Phone;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "tb_person")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class Person implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    @Embedded
    private Address address;
    @Embedded
    private Email email;
    @Embedded
    private Phone phone;
    @Embedded
    private BirthDate birthDate;

    public Person(Address address, Email email, Phone phone, BirthDate birthDate) {
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
    }
}
