package com.manager.coomanager.contractModule.entities;
import com.manager.coomanager.shared.domain.person.Person;
import com.manager.coomanager.shared.domain.person.NaturalPerson;
import com.manager.coomanager.shared.domain.person.JuridicPerson;

import com.manager.coomanager.contractModule.entities.Contract;
import com.manager.coomanager.contractModule.enums.InstitutionSphere;
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
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_institution")
@Filter(name = "tenantFilter", condition = "cooperative_id = :cooperativeId")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Institution extends TenantEntity implements Serializable {

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
        super();
        this.institutionSphere = institutionSphere;
        this.juridicPerson = juridicPerson;
        this.status = Status.ACTIVE;
        this.uuid = UUID.randomUUID();
        if (this.getCooperativeId() == null && juridicPerson != null && juridicPerson.getCooperativeId() != null) {
            this.setCooperativeId(juridicPerson.getCooperativeId());
        }
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
