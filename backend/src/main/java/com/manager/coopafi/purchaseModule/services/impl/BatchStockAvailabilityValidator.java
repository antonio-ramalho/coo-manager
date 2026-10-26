package com.manager.coopafi.purchaseModule.services.impl;

import com.manager.coopafi.infrastructure.exceptions.DomainException;
import com.manager.coopafi.purchaseModule.entities.InputBatch;
import com.manager.coopafi.purchaseModule.enums.ProductInventoryStatus;
import com.manager.coopafi.purchaseModule.services.validator.PurchaseItemValidationStrategy;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Valida a disponibilidade física de estoque no lote e se a quantidade é
 * positiva.
 */
@Component
@Order(1)
public class BatchStockAvailabilityValidator implements PurchaseItemValidationStrategy {

    @Override
    public void validate(InputBatch batch, Double requestedQuantity) {
        if (requestedQuantity == null || requestedQuantity <= 0) {
            throw new DomainException("A quantidade do item deve ser estritamente maior que zero.");
        }

        if (batch.getProductStatus() == ProductInventoryStatus.OUT_OF_STOCK) {
            throw new DomainException(String.format("Lote %s está esgotado (sem estoque).", batch.getBatchCode()));
        }

        BigDecimal currentStock = batch.getCurrentQuantity() != null
                ? batch.getCurrentQuantity().getAmount()
                : BigDecimal.ZERO;

        if (currentStock.compareTo(BigDecimal.valueOf(requestedQuantity)) < 0) {
            throw new DomainException(String.format(
                    "Saldo insuficiente no lote %s. Disponível: %s, Solicitado: %s",
                    batch.getBatchCode(), currentStock, requestedQuantity));
        }
    }
}
