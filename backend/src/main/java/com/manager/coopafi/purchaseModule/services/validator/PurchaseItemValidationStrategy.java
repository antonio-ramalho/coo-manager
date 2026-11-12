package com.manager.coopafi.purchaseModule.services.validator;

import com.manager.coopafi.purchaseModule.entities.InputBatch;

/**
 * Strategy para validação de regras de negócio ao adicionar um item de lote em
 * uma compra/venda.
 */
public interface PurchaseItemValidationStrategy {

    /**
     * @param batch
     * @param requestedQuantity
     */
    void validate(InputBatch batch, Double requestedQuantity);
}
