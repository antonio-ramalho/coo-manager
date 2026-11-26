package com.manager.coomanager.farmerModule.entities;
import com.manager.coomanager.shared.domain.person.Person;
import com.manager.coomanager.shared.domain.person.NaturalPerson;
import com.manager.coomanager.shared.domain.person.JuridicPerson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.manager.coomanager.contractModule.entities.Contract;
import com.manager.coomanager.contractModule.entities.FarmerContract;
import com.manager.coomanager.shared.domain.valueobjects.Price;
import com.manager.coomanager.shared.domain.enums.Status;
import com.manager.coomanager.shared.domain.entities.TenantEntity;
import org.hibernate.annotations.Filter;
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
@Filter(name = "tenantFilter", condition = "cooperative_id = :cooperativeId")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Farmer extends TenantEntity implements Serializable {

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
        super();
        this.person = Objects.requireNonNull(person);
        this.status = Status.ACTIVE;
        this.uuid = UUID.randomUUID();
        if (this.getCooperativeId() == null && person.getCooperativeId() != null) {
            this.setCooperativeId(person.getCooperativeId());
        }
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
