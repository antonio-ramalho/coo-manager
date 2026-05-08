package com.manager.coopafi.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.manager.coopafi.domain.valueObjects.Price;
import com.manager.coopafi.domain.valueObjects.Quantity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_input_purchase_item")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InputPurchaseItem extends BaseReceiptItem <InputPurchase> {

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "purchase_id")
    private InputPurchase inputPurchase;

    @ManyToOne
    @JoinColumn(name = "input_batch_id")
    private InputBatch inputBatch;

    public InputPurchaseItem(Quantity quantity, Price appliedPrice, InputBatch inputBatch) {
        super(quantity, appliedPrice);
        this.inputBatch = inputBatch;
    }

    @Override
    protected void linkToBaseReceipt(InputPurchase receipt) {
        this.inputPurchase = receipt;
    }
}
