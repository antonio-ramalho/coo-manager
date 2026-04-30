package com.manager.coopafi.domain.entities;

import com.manager.coopafi.domain.valueObjects.Quantity;
import com.manager.coopafi.exceptions.DomainException;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "tb_farmer_item_quotas")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class FarmerItemQuota implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne
    @JoinColumn(name = "farmer_contract_id")
    private FarmerContract farmerContract;

    @ManyToOne
    @JoinColumn(name = "contracted_product_id")
    private ContractedProduct contractedProduct;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "max_quantity"))
    private Quantity maxQuantity;
    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "delivery_quantity"))
    private Quantity deliveryQuantity;

    public FarmerItemQuota(FarmerContract farmerContract, ContractedProduct contractedProduct, Quantity maxQuantity) {
        this.farmerContract = Objects.requireNonNull(farmerContract,  "É preciso existir um agricultor");
        this.contractedProduct = Objects.requireNonNull(contractedProduct, "É preciso existir um produto contratado");
        this.maxQuantity = maxQuantity;
        this.deliveryQuantity = new Quantity(BigDecimal.ZERO);
    }

    public void registerDelivery(Quantity deliveryQuantity) {
        if (this.maxQuantity != null) {
            Quantity projectedQuantity = this.deliveryQuantity.add(deliveryQuantity);
            if (projectedQuantity.compareTo(this.maxQuantity) > 0) {
                throw new DomainException("A entrega excede a cota individual do agricultor neste contrato.");
            }
        }
        this.deliveryQuantity = this.deliveryQuantity.add(deliveryQuantity);
    }
}
