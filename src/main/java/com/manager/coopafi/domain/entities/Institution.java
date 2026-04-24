package com.manager.coopafi.domain.entities;

import com.manager.coopafi.enums.InstitutionSphere;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "tb_institution")
@Getter
@Setter
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

    @Enumerated(EnumType.STRING)
    private InstitutionSphere institutionSphere;

    public Institution(InstitutionSphere institutionSphere, JuridicPerson juridicPerson) {
        this.institutionSphere = institutionSphere;
        this.juridicPerson = juridicPerson;
    }
}
