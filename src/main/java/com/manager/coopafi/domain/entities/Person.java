package com.manager.coopafi.domain.entities;

import com.manager.coopafi.domain.valueObjects.Address;
import com.manager.coopafi.domain.valueObjects.BirthDate;
import com.manager.coopafi.domain.valueObjects.Email;
import com.manager.coopafi.domain.valueObjects.Phone;
import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "tb_person")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Person implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Address address;
    @Embedded
    private Email email;
    @Embedded
    private Phone phone;
    @Embedded
    private BirthDate birthDate;

    protected Person() {}

    public Person(Address address, Email email, Phone phone, BirthDate birthDate) {
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
    }

    public Address getAddress() {
        return address;
    }

    public Email getEmail() {
        return email;
    }

    public Long getId() {
        return id;
    }

    public Phone getPhone() {
        return phone;
    }

    public BirthDate getBirthDate() {
        return birthDate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
