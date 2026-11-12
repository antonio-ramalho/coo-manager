package com.manager.coomanager.cooperativeModule.entities;

import com.manager.coomanager.infrastructure.valueObjects.*;
import com.manager.coomanager.personModule.enums.Status;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tb_cooperative")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Cooperative implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Column(updatable = false, unique = true, nullable = false)
    private UUID uuid;

    @Column(nullable = false)
    private String legalName;

    @Column(nullable = false)
    private String tradeName;

    @Embedded
    private Cnpj cnpj;

    @Embedded
    private StateRegistration stateRegistration;

    @Embedded
    private Email email;

    @Embedded
    private Phone phone;

    @Embedded
    private Address address;

    private LocalDate foundationDate;

    @Column(nullable = false, updatable = false)
    private LocalDateTime registrationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    public Cooperative(Address address, Email email, Phone phone, LocalDate foundationDate,
            Cnpj cnpj, String legalName, String tradeName, StateRegistration stateRegistration) {
        this.address = Objects.requireNonNull(address, "O endereço não pode ser nulo.");
        this.email = Objects.requireNonNull(email, "O e-mail não pode ser nulo.");
        this.phone = Objects.requireNonNull(phone, "O telefone não pode ser nulo.");
        this.cnpj = Objects.requireNonNull(cnpj, "O CNPJ não pode ser nulo.");
        this.legalName = Objects.requireNonNull(legalName, "A razão social não pode ser nula.");
        this.tradeName = Objects.requireNonNull(tradeName, "O nome fantasia não pode ser nulo.");
        this.stateRegistration = Objects.requireNonNull(stateRegistration, "A Inscrição Estadual não pode ser nula.");
        this.foundationDate = foundationDate;
        this.uuid = UUID.randomUUID();
        this.registrationDate = LocalDateTime.now();
        this.status = Status.ACTIVE;
    }

    public void updateName(String name) {
        this.legalName = Objects.requireNonNull(name, "A razão social não pode ser nula.");
    }

    public void updateTradeName(String tradeName) {
        this.tradeName = Objects.requireNonNull(tradeName, "O nome fantasia não pode ser nulo.");
    }

    public void updateEmail(Email email) {
        this.email = Objects.requireNonNull(email, "O e-mail não pode ser nulo.");
    }

    public void updatePhone(Phone phone) {
        this.phone = Objects.requireNonNull(phone, "O telefone não pode ser nulo.");
    }

    public void updateAddress(Address address) {
        this.address = Objects.requireNonNull(address, "O endereço não pode ser nulo.");
    }

    public void updateFoundationDate(LocalDate foundationDate) {
        this.foundationDate = foundationDate;
    }

    public void updateStateRegistration(StateRegistration stateRegistration) {
        this.stateRegistration = Objects.requireNonNull(stateRegistration, "A Inscrição Estadual não pode ser nula.");
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
}
