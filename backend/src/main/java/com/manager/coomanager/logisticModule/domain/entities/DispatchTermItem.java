package com.manager.coomanager.logisticModule.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.manager.coomanager.contractModule.entities.ContractedProduct;
import com.manager.coomanager.baseModule.BaseReceiptItem;
import com.manager.coomanager.infrastructure.valueObjects.Price;
import com.manager.coomanager.infrastructure.valueObjects.Quantity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_dispatch_term_items")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DispatchTermItem extends BaseReceiptItem<DispatchTerm> {

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "dispatch_term_id")
    private DispatchTerm dispatchTerm;

    @ManyToOne
    @JoinColumn(name = "contracted_product_id")
    private ContractedProduct contractedProduct;

    public DispatchTermItem(Quantity quantity, Price appliedPrice, ContractedProduct contractedProduct) {
        super(quantity, appliedPrice);
        this.contractedProduct = contractedProduct;
    }

    @Override
    protected void linkToBaseReceipt(DispatchTerm receipt) {
        this.dispatchTerm = receipt;
    }
}