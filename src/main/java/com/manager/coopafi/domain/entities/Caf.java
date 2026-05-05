package com.manager.coopafi.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.manager.coopafi.domain.valueObjects.CafNumber;
import com.manager.coopafi.enums.DocumentStatus;
import com.manager.coopafi.enums.SettlementType;
import com.manager.coopafi.exceptions.DomainException;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_caf")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Caf implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    @Embedded
    private CafNumber cafNumber;
    @Enumerated(EnumType.STRING)
    private SettlementType settlementType;

    @Enumerated(EnumType.STRING)
    private DocumentStatus documentStatus;

    private LocalDate expirationDate;
    @JsonIgnore
    @OneToMany(mappedBy = "caf")
    private List<Farmer> farmers = new ArrayList<>();

    public Caf(CafNumber cafNumber, LocalDate expirationDate, SettlementType settlementType) {
        validateExpiration(expirationDate);
        this.cafNumber = cafNumber;
        this.expirationDate = expirationDate;
        this.settlementType = settlementType;
        this.documentStatus = DocumentStatus.ACTIVE;
    }

    private void validateExpiration(LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new DomainException("Não é possível registrar uma CAF já vencida.");
        }
    }

    public boolean isValid() {
        if (this.documentStatus != DocumentStatus.ACTIVE) {
            return false;
        }
        return expirationDate.isAfter(LocalDate.now());
    }

    public void updateStatusByDate() {
        if (expirationDate.isBefore(LocalDate.now())) {
            this.documentStatus = DocumentStatus.EXPIRED;
        }
    }

    public void addFarmer(Farmer farmer) {
        if (farmer.getCaf() != null && !farmer.getCaf().equals(this)) {
            throw new DomainException("O farmer já está vinculado a outra CAF: "
                    + farmer.getCaf().getId()
                    + ". É necessário desvinculá-lo primeiro.");
        }
        this.farmers.add(farmer);
        farmer.linkCaf(this);
    }

    public void removeFarmer(Farmer farmer) {
        if (!this.farmers.contains(farmer) || !this.equals(farmer.getCaf())) {
            throw new DomainException("Operação inválida: Este agricultor não pertence a esta CAF.");
        }
        this.farmers.remove(farmer);
        farmer.linkCaf(null);
    }

    public void renewDocument(LocalDate newExpirationDate) {
        expirationDate = newExpirationDate;
        this.documentStatus = DocumentStatus.ACTIVE;
    }

    public void suspendDocument() {
        this.documentStatus = DocumentStatus.SUSPENDED;
    }
}
