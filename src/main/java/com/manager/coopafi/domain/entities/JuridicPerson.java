package com.manager.coopafi.domain.entities;

import com.manager.coopafi.domain.valueObjects.*;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "tb_juridic_person")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class JuridicPerson extends Person {

    @Embedded
    private Cnpj cnpj;
    private String tradeName;
    private String legalName;

    public JuridicPerson(Address address, Email email, Phone phone, BirthDate birthDate,
                         Cnpj cnpj, String legalName, String tradeName) {
        super(address, email, phone, birthDate);
        this.cnpj = cnpj;
        this.legalName = legalName;
        this.tradeName = tradeName;
    }

    public void updateTradeName(String tradeName) {
        this.tradeName = Objects.requireNonNull(tradeName);
    }

    public void updateLegalName(String legalName) {
        this.legalName = Objects.requireNonNull(legalName);
    }
}
