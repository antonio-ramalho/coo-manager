package com.manager.coopafi.domain.entities;

import com.manager.coopafi.enums.InstitutionSphere;
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

    @OneToOne
    @JoinColumn(name = "jur_person")
    private JuridicPerson juridicPerson;

    @OneToMany(mappedBy = "institution", cascade = CascadeType.PERSIST)
    private List<Contract> contracts = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private InstitutionSphere institutionSphere;

    public Institution(InstitutionSphere institutionSphere, JuridicPerson juridicPerson) {
        this.institutionSphere = institutionSphere;
        this.juridicPerson = juridicPerson;
    }

    public List<Contract> getContracts() {
        return Collections.unmodifiableList(contracts);
    }
}
