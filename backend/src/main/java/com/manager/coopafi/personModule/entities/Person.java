package com.manager.coopafi.personModule.entities;

import com.manager.coopafi.infrastructure.valueObjects.Address;
import com.manager.coopafi.infrastructure.valueObjects.Email;
import com.manager.coopafi.infrastructure.valueObjects.Phone;
import com.manager.coopafi.personModule.enums.PersonType;
import com.manager.coopafi.personModule.enums.Status;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tb_person")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class Person implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String legalName;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private PersonType personType;

    @Embedded
    @Column(unique = true)
    private Email email;

    @EqualsAndHashCode.Include
    @Column(updatable = false, unique = true, nullable = false)
    private UUID uuid;

    @Embedded
    private Phone phone;

    @Enumerated(EnumType.STRING)
    private Status status;

    public Person(String legalName, Address address, Email email, Phone phone, PersonType personType) {
        this.legalName = Objects.requireNonNull(legalName);
        this.address = Objects.requireNonNull(address);
        this.email = Objects.requireNonNull(email);
        this.phone = Objects.requireNonNull(phone);
        this.status = Status.ACTIVE;
        this.uuid = UUID.randomUUID();
        this.personType = personType;
    }

    public void updateAddress(Address address) {
        this.address = Objects.requireNonNull(address);
    }

    public void updateEmail(Email email) {
        this.email = Objects.requireNonNull(email);
    }

    public void updatePhone(Phone phone) {
        this.phone = Objects.requireNonNull(phone);
    }

    public void updateName(String name) {
        this.legalName = Objects.requireNonNull(name);
    }

    public void deactivate() {
        this.status = Status.INACTIVE;
    }

    public void activate() {
        this.status = Status.ACTIVE;
    }

    public void suspend() {
        this.status = Status.SUSPENDED;
    }

    public abstract String getDocumentNumber();
}