package com.manager.coopafi.domain.entities;

import com.manager.coopafi.domain.valueObjects.Price;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "contract_id")
    private Contract contract;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private AgriculturalProduct product;

    @OneToMany(mappedBy = "contractedProduct", cascade = CascadeType.PERSIST)
    private List<FarmerItemQuota>  farmerItemQuotas;

    public ContractedProduct(Contract contract, AgriculturalProduct product, Price fixedPrice, String contractedName) {
        this.contract = contract;
        this.product = product;
        this.fixedPrice = fixedPrice;
        this.contractedName = contractedName;
    }
}
