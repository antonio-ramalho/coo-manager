package com.manager.coopafi.domain.entities;

import com.manager.coopafi.domain.valueObjects.Price;
import com.manager.coopafi.enums.DocumentStatus;
import com.manager.coopafi.exceptions.DomainException;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tb_farmer_contracts")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class FarmerContract implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "specific_cota_value"))
    private Price specificCota;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "accumulated_value"))
    private Price accumulatedValue;
    private LocalDate adhesionDate;
    @Enumerated(EnumType.STRING)
    private DocumentStatus documentStatus;

    @ManyToOne
    @JoinColumn(name = "contract_id")
    private Contract contract;

    @ManyToOne
    @JoinColumn(name = "farmer_id")
    private Farmer farmer;

    @OneToMany(mappedBy = "farmerContract", cascade = CascadeType.PERSIST)
    private List<FarmerItemQuota> farmerItemQuotas = new ArrayList<>();

    public FarmerContract(Contract contract, Farmer farmer, Price specificCota) {
        this.contract = Objects.requireNonNull(contract, "O contrato é obrigatório.");
        this.farmer = Objects.requireNonNull(farmer, "O agricultor é obrigatório.");
        this.specificCota = specificCota;
        this.accumulatedValue = new Price(BigDecimal.ZERO);
        this.adhesionDate = LocalDate.now();
        this.documentStatus = DocumentStatus.ACTIVE;
    }

    public void registerDelivery(Price deliveryValue) {
        if (this.specificCota != null) {
            Price projectedValue = this.accumulatedValue.add(deliveryValue);
            if (projectedValue.getValue().compareTo(this.specificCota.getValue()) > 0) {
                throw new DomainException("A entrega excede a cota individual do agricultor neste contrato.");
            }
        }
        this.accumulatedValue = this.accumulatedValue.add(deliveryValue);
    }

    public void addProductQuota(ContractedProduct product, Double maxQuantity) {
        FarmerItemQuota quota = new FarmerItemQuota(this, product, maxQuantity);
        this.farmerItemQuotas.add(quota);
    }
}
