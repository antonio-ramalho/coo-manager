package com.manager.coopafi.personModule.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.manager.coopafi.infrastructure.valueObjects.CafNumber;
import com.manager.coopafi.infrastructure.valueObjects.ExpirationDate;
import com.manager.coopafi.personModule.enums.DocumentStatus;
import com.manager.coopafi.personModule.enums.SettlementType;
import com.manager.coopafi.infrastructure.exceptions.DomainException;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    private Long id;

    @EqualsAndHashCode.Include
    @Column(nullable = false, unique = true, updatable = false)
    private UUID uuid;

    @Embedded
    private CafNumber cafNumber;

    @Enumerated(EnumType.STRING)
    private SettlementType settlementType;

    @Enumerated(EnumType.STRING)
    private DocumentStatus documentStatus;

    private ExpirationDate expirationDate;

    @JsonIgnore
    @OneToMany(mappedBy = "caf")
    private List<Farmer> farmers = new ArrayList<>();

    public Caf(CafNumber cafNumber, ExpirationDate expirationDate, SettlementType settlementType) {
        this.cafNumber = cafNumber;
        this.expirationDate = expirationDate;
        this.settlementType = settlementType;
        this.documentStatus = DocumentStatus.ACTIVE;
        this.uuid = UUID.randomUUID();
    }

    public boolean isValid() {
        if (this.documentStatus != DocumentStatus.ACTIVE) {
            return false;
        }
        return this.expirationDate.isValid();
    }

    public void updateStatusByDate() {
        if (this.expirationDate.isExpired()) {
            this.documentStatus = DocumentStatus.EXPIRED;
        }
    }

    public void suspendDocument() {
        this.documentStatus = DocumentStatus.SUSPENDED;
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

    public void renewDocument(ExpirationDate newExpirationDate) {
        this.expirationDate = newExpirationDate;
        this.documentStatus = DocumentStatus.ACTIVE;
    }
}
