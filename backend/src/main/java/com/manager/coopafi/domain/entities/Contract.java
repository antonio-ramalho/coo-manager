package com.manager.coopafi.domain.entities;

import com.manager.coopafi.domain.valueObjects.Price;
import com.manager.coopafi.domain.valueObjects.Quantity;
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
import java.util.*;

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

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContractConsumer> contractConsumers = new ArrayList<>();

    public Contract(LocalDate finalContractDate, Price totalContractValue, Price globalLimit,
                    ParticipationLimitRule participationRule, ProductDeliveryRule productDeliveryRule,
                    Institution institution) {
        this.initialContractDate = LocalDate.now();
        this.finalContractDate = Objects.requireNonNull(finalContractDate, "A data final é obrigatória.");
        this.totalContractValue = Objects.requireNonNull(totalContractValue, "O valor total do contrato é obrigatório.");
        this.globalLimit = globalLimit;
        this.participationRule = participationRule;
        this.productDeliveryRule = productDeliveryRule;
        this.institution = Objects.requireNonNull(institution, "Um contrato deve estar vinculado a uma instituição.");
        this.usedContractValue = new Price(BigDecimal.ZERO);
        this.documentStatus = DocumentStatus.ACTIVE;
    }

    public void deactivate() {
        this.documentStatus = DocumentStatus.INACTIVE;
    }

    public void addContractedProduct(AgriculturalProduct baseProduct, Price fixedPrice,
                                     String contractedName, Quantity quantity) {
        ContractedProduct contractedProduct = new ContractedProduct(this, baseProduct, fixedPrice, contractedName, quantity);
        this.products.add(contractedProduct);
    }

    public void addConsumerUnit(ConsumerUnit unit) {
        if (unit == null) return;

        boolean alreadyExists = this.contractConsumers.stream()
                .anyMatch(cc -> cc.getConsumer().getId().equals(unit.getId()));

        if (alreadyExists) {
            throw new DomainException("A unidade consumidora " + unit.getJuridicPerson().getTradeName() + " já está vinculada a este contrato.");
        }

        ContractConsumer association = new ContractConsumer(this, unit);
        this.contractConsumers.add(association);
        unit.getContractConsumers().add(association);
    }

    public FarmerContract addFarmerParticipation(Farmer farmer, Price specificCota) {

        boolean alreadyExists = this.farmerContracts.stream()
                .anyMatch(fc -> fc.getFarmer().getId().equals(farmer.getId()));

        if (alreadyExists) {
            throw new DomainException("O agricultor " + farmer.getPerson().getName() + " já está vinculado a este contrato.");
        }

        this.participationRule.validate(this, farmer, specificCota);
        FarmerContract participation = new FarmerContract(this, farmer, specificCota);
        this.farmerContracts.add(participation);
        farmer.getFarmerContracts().add(participation);
        return participation;
    }

    public Price calculateBalance() {
        return this.totalContractValue.subtract(this.usedContractValue);
    }

    public void validateContractIntegrity() {

        for (FarmerContract fc : this.farmerContracts) {
            fc.validateProductAllocation();
        }

        this.validateStockValue();
        this.validateQuotas();
        this.validateGlobalProductLimits();
    }

    private void validateQuotas() {

        Price accumulate = this.farmerContracts.stream()
                .map(FarmerContract::getSpecificCota)
                .reduce(new Price(BigDecimal.ZERO), Price::add);

        if (accumulate.getValue().compareTo(this.totalContractValue.getValue()) > 0) {
            throw new DomainException("A soma de todas as quotas dos agricultores supera o valor do contrato.");
        }

        if (accumulate.getValue().compareTo(this.totalContractValue.getValue()) < 0) {
            throw new DomainException("A soma de todas as quotas dos agricultores é inferior ao valor do contrato.");
        }
    }

    private void validateGlobalProductLimits() {
        if (this.productDeliveryRule == ProductDeliveryRule.VALUE_ONLY) {
            return;
        }

        for (ContractedProduct contractedProduct : this.products) {
            Long productIdBase = contractedProduct.getProduct().getId();

            Quantity totalDistributed = this.farmerContracts.stream()
                    .flatMap(fc -> fc.getFarmerItemQuotas().stream())
                    .filter(quota -> quota.getContractedProduct().getProduct().getId().equals(productIdBase))
                    .map(FarmerItemQuota::getMaxQuantity)
                    .reduce(new Quantity(BigDecimal.ZERO), Quantity::add);

            int comparison = totalDistributed.compareTo(contractedProduct.getQuantity());

            if (comparison > 0) {
                throw new DomainException("A quantidade distribuída de "
                        + contractedProduct.getContractedName()
                        + " excede o limite global do contrato.");
            }
        }
    }

    private void validateStockValue() {
        Price total = this.products.stream()
                .map(ContractedProduct::calculateProductValue)
                .reduce(new Price(BigDecimal.ZERO), Price::add);

        if (total.getValue().compareTo(this.totalContractValue.getValue()) > 0) {
            throw new DomainException("A soma do valor alocado para todos os produtos ultrapassa o valor do contrato.");
        }

        if (total.getValue().compareTo(this.totalContractValue.getValue()) < 0) {
            throw new DomainException("A soma do valor alocado para todos os produtos é inferior ao valor do contrato.");
        }
    }

    public void incrementUsedValue(Price deliveryValue) {
        Price projectedValue = this.usedContractValue.add(deliveryValue);
        if (projectedValue.getValue().compareTo(this.totalContractValue.getValue()) > 0) {
            throw new DomainException("O valor total do contrato (edital) foi atingido.");
        }
        this.usedContractValue = projectedValue;
    }
}