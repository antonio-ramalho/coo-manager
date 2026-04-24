package com.manager.coopafi.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.manager.coopafi.domain.valueObjects.Price;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "tb_input_purchase_item")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class InputPurchaseItem implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "purchase_id")
    private InputPurchase inputPurchase;

    private Integer quantity;
    @Embedded
    private Price appliedPrice;
    @Embedded
    private Price totalPrice;

    @ManyToOne
    @JoinColumn(name = "input_batch_id")
    private InputBatch inputBatch;

    public InputPurchaseItem(Price appliedPrice, InputBatch inputBatch, Integer quantity) {
        this.appliedPrice = appliedPrice;
        this.inputBatch = inputBatch;
        this.quantity = quantity;
        this.totalPrice = calculateTotalPrice(quantity, appliedPrice);
        this.inputBatch.decreaseQuantity(quantity);
    }

    public Price calculateTotalPrice(Integer quantity, Price appliedPrice) {
        BigDecimal newQuantity = BigDecimal.valueOf(quantity);
        return appliedPrice.multiply(newQuantity);
    }
}
