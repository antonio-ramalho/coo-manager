package com.manager.coopafi.domain.entities;

import com.manager.coopafi.domain.valueObjects.Price;
import com.manager.coopafi.enums.DocumentStatus;
import com.manager.coopafi.enums.ParticipationLimitRule;
import com.manager.coopafi.enums.ProductDeliveryRule;
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
@Table(name = "tb_contracts")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Contract implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private LocalDate initialContractDate;
    private LocalDate finalContractDate;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "total_contract_value"))
    private Price totalContractValue;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "used_contract_value"))
    private Price usedContractValue;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "global_limit_value"))
    private Price globalLimit;

    @Enumerated(EnumType.STRING)
    private ParticipationLimitRule participationRule;

    @Enumerated(EnumType.STRING)
    private ProductDeliveryRule productDeliveryRule;

    @Enumerated(EnumType.STRING)
    private DocumentStatus documentStatus;

    @ManyToOne
    @JoinColumn(name = "institution_id")
    private Institution institution;

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FarmerContract> farmerContracts = new ArrayList<>();

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContractedProduct> products = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "tb_contract_consumers",
            joinColumns = @JoinColumn(name = "contract_id"),
            inverseJoinColumns = @JoinColumn(name = "consumer_unit_id")
    )
    private List<ConsumerUnit> consumers = new ArrayList<>();

    public Contract(LocalDate finalContractDate, Price totalContractValue, Price globalLimit,
                    ParticipationLimitRule participationRule, ProductDeliveryRule productDeliveryRule,
                    Institution institution) {
        this.initialContractDate = LocalDate.now();
        this.finalContractDate = Objects.requireNonNull(finalContractDate, "A data final é obrigatória.");
        this.totalContractValue = Objects.requireNonNull(totalContractValue, "O valor total do contrato é obrigatório.");
        this.globalLimit = Objects.requireNonNull(globalLimit, "O limite global por agricultor é obrigatório.");
        this.participationRule = participationRule;
        this.productDeliveryRule = productDeliveryRule;
        this.institution = Objects.requireNonNull(institution, "Um contrato deve estar vinculado a uma instituição.");

        this.usedContractValue = new Price(BigDecimal.ZERO);
        this.documentStatus = DocumentStatus.ACTIVE;
    }

    public void addContractedProduct(AgriculturalProduct baseProduct, Price fixedPrice, String contractedName) {
        ContractedProduct contractedProduct = new ContractedProduct(this, baseProduct, fixedPrice, contractedName);
        this.products.add(contractedProduct);
    }

    public void addConsumerUnit(ConsumerUnit unit) {
        if (unit == null) return;
        if (!this.consumers.contains(unit)) {
            this.consumers.add(unit);
            unit.getContracts().add(this);
        }
    }

    public void addFarmerParticipation(Farmer farmer, Price specificCota) {
        FarmerContract participation = new FarmerContract(this, farmer, specificCota);
        this.farmerContracts.add(participation);
        farmer.getFarmerContracts().add(participation);
    }

    public void incrementUsedValue(Price deliveryValue) {
        Price projectedValue = this.usedContractValue.add(deliveryValue);
        if (projectedValue.getValue().compareTo(this.totalContractValue.getValue()) > 0) {
            throw new DomainException("O valor total do contrato (edital) foi atingido.");
        }
        this.usedContractValue = projectedValue;
    }
}
