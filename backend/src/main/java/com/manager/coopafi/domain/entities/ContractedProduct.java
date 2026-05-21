package com.manager.coopafi.domain.entities;

import com.manager.coopafi.domain.valueObjects.Price;
import com.manager.coopafi.domain.valueObjects.Quantity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tb_contracted_products")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ContractedProduct implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String contractedName;
    @Embedded
    private Price fixedPrice;

    @Embedded
    private Quantity quantity;

    @ManyToOne
    @JoinColumn(name = "contract_id")
    private Contract contract;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private AgriculturalProduct product;

    @OneToMany(mappedBy = "contractedProduct")
    private List<FarmerItemQuota>  farmerItemQuotas;

    public ContractedProduct(Contract contract, AgriculturalProduct product, Price fixedPrice,
                             String contractedName, Quantity quantity) {
        this.contract = Objects.requireNonNull(contract);
        this.product = Objects.requireNonNull(product);
        this.fixedPrice = Objects.requireNonNull(fixedPrice);
        this.contractedName = Objects.requireNonNull(contractedName);
        this.quantity = Objects.requireNonNull(quantity);
    }

    public Price calculateProductValue() {
        return this.fixedPrice.multiply(this.quantity.getAmount());
    }
}
