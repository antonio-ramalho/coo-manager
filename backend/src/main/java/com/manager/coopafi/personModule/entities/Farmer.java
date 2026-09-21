package com.manager.coopafi.personModule.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.manager.coopafi.contractModule.entities.Contract;
import com.manager.coopafi.contractModule.entities.FarmerContract;
import com.manager.coopafi.infrastructure.valueObjects.Price;
import com.manager.coopafi.personModule.enums.Status;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tb_farmer")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Farmer implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Column(updatable = false, unique = true, nullable = false)
    private UUID uuid;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToOne
    @JoinColumn(name = "nat_person")
    private NaturalPerson person;

    @ManyToOne
    @JoinColumn(name = "caf_id")
    private Caf caf;

    @ManyToOne
    @JoinColumn(name = "certificate_id")
    private OrganicCertificate certificate;

    @JsonIgnore
    @OneToMany(mappedBy = "farmer")
    private List<FarmerContract> farmerContracts = new ArrayList<>();

    public Farmer(NaturalPerson person) {
        this.person = Objects.requireNonNull(person);
        this.status = Status.ACTIVE;
        this.uuid = UUID.randomUUID();
    }

    public void deactivate() {
        this.status = Status.INACTIVE;
    }

    public void linkCaf(Caf caf) {
        this.caf = caf;
    }

    public void linkCertificate(OrganicCertificate certificate) {
        this.certificate = certificate;
    }

    public void enrollInContract(Contract contract, Price specificCota) {
        FarmerContract participation = new FarmerContract(contract, this, specificCota);
        this.farmerContracts.add(participation);
    }
}
