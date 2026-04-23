package com.manager.coopafi.domain.entities;

import com.manager.coopafi.domain.valueObjects.*;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_juridic_person")
public class JuridicPerson extends Person {

    @Embedded
    private Cnpj cnpj;
    private String tradeName;
    private String legalName;

    protected JuridicPerson() {}

    public JuridicPerson(Address address, Email email, Phone phone, BirthDate birthDate,
                         Cnpj cnpj, String legalName, String tradeName) {
        super(address, email, phone, birthDate);
        this.cnpj = cnpj;
        this.legalName = legalName;
        this.tradeName = tradeName;
    }

    public Cnpj getCnpj() {
        return cnpj;
    }

    public String getLegalName() {
        return legalName;
    }

    public String getTradeName() {
        return tradeName;
    }
}
