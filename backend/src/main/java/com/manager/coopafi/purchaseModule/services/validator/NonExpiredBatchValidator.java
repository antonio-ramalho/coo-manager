package com.manager.coopafi.purchaseModule.services.validator;

import com.manager.coopafi.infrastructure.exceptions.DomainException;
import com.manager.coopafi.purchaseModule.entities.InputBatch;
import com.manager.coopafi.purchaseModule.enums.ProductInventoryStatus;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Valida se o lote não se encontra vencido para comercialização.
 */
@Component
@Order(2)
public class NonExpiredBatchValidator implements PurchaseItemValidationStrategy {

    @Override
    public void validate(InputBatch batch, Double requestedQuantity) {
        if (batch.getProductStatus() == ProductInventoryStatus.EXPIRED) {
            throw new DomainException(
                    String.format("Lote %s com status vencido não pode ser comercializado.", batch.getBatchCode()));
        }

        if (batch.getExpirationDate() != null && batch.getExpirationDate().getValue() != null) {
            if (batch.getExpirationDate().getValue().isBefore(LocalDate.now())) {
                throw new DomainException(String.format(
                        "Lote %s está vencido desde %s e não pode ser comercializado.",
                        batch.getBatchCode(), batch.getExpirationDate().getValue()));
            }
        }
    }
}
