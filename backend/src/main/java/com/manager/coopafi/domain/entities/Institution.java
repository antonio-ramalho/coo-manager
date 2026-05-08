package com.manager.coopafi.domain.entities;

import com.manager.coopafi.enums.InstitutionSphere;
import com.manager.coopafi.enums.UserStatus;
import com.manager.coopafi.exceptions.DomainException;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    @EqualsAndHashCode.Include
    private Long id;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

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
        this.status = UserStatus.ACTIVE;
    }

    public void deactivate() {
        if (this.status == UserStatus.INACTIVE) {
            throw new DomainException("Esta Instituição já está inativa.");
        }
        this.status = UserStatus.INACTIVE;
    }

    public List<Contract> getContracts() {
        return Collections.unmodifiableList(contracts);
    }
}
