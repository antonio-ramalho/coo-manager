package com.manager.coopafi.personModule.entities;

import com.manager.coopafi.contractModule.entities.Contract;
import com.manager.coopafi.personModule.enums.InstitutionSphere;
import com.manager.coopafi.personModule.enums.Status;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_institution")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Institution implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Column(updatable = false, nullable = false, unique = true)
    private UUID uuid;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToOne
    @JoinColumn(name = "jur_person")
    private JuridicPerson juridicPerson;

    @OneToMany(mappedBy = "institution", cascade = CascadeType.PERSIST, orphanRemoval = false)
    private List<Contract> contracts = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private InstitutionSphere institutionSphere;

    public Institution(InstitutionSphere institutionSphere, JuridicPerson juridicPerson) {
        this.institutionSphere = institutionSphere;
        this.juridicPerson = juridicPerson;
        this.status = Status.ACTIVE;
        this.uuid = UUID.randomUUID();
    }

    public List<Contract> getContracts() {
        return Collections.unmodifiableList(contracts);
    }

    public void deactivate() {
        this.status = Status.INACTIVE;
    }

    public void  activate() {
        this.status = Status.ACTIVE;
    }

    public void suspend() {
        this.status = Status.SUSPENDED;
    }
}
