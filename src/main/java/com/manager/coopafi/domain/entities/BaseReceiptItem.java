package com.manager.coopafi.domain.entities;

import com.manager.coopafi.domain.valueObjects.Price;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@MappedSuperclass
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class BaseReceiptItem <T extends BaseReceipt<?>> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    private Integer quantity;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "total_price_value"))
    private Price totalPrice;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "applied_price"))
    private Price appliedPrice;

    public BaseReceiptItem(Integer quantity, Price appliedPrice) {
        this.quantity = quantity;
        this.appliedPrice = appliedPrice;
        this.totalPrice = calculateTotalPrice(quantity, appliedPrice);
    }

    private Price calculateTotalPrice(Integer quantity, Price appliedPrice) {
        BigDecimal newQuantity = BigDecimal.valueOf(quantity);
        return appliedPrice.multiply(newQuantity);
    }

    protected abstract void linkToBaseReceipt(T receipt);
}
