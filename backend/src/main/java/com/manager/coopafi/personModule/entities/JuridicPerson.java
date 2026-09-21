package com.manager.coopafi.personModule.entities;

import com.manager.coopafi.infrastructure.valueObjects.*;
import com.manager.coopafi.personModule.enums.PersonType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "tb_juridic_person")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class JuridicPerson extends Person {

    @Embedded
    @Column(unique = true)
    private Cnpj cnpj;
    private String tradeName;
    private LocalDate foundationDate;

    public JuridicPerson(Address address, Email email, Phone phone, LocalDate foundationDate,
                         Cnpj cnpj, String legalName, String tradeName) {
        super(legalName, address, email, phone, PersonType.JURIDIC);
        this.cnpj = Objects.requireNonNull(cnpj);
        this.tradeName = Objects.requireNonNull(tradeName);
        this.foundationDate = Objects.requireNonNull(foundationDate);
    }

    public void updateTradeName(String tradeName) {
        this.tradeName = Objects.requireNonNull(tradeName);
    }

    public void updateFoundationDate(LocalDate foundationDate) {
        this.foundationDate = Objects.requireNonNull(foundationDate);
    }

    @Override
    public String getDocumentNumber() {
        return this.cnpj != null ? this.cnpj.getCnpjNumber() : null;
    }
}
