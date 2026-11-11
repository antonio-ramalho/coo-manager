package com.manager.coopafi.purchaseModule.dto.inputPurchase;

import com.manager.coopafi.purchaseModule.entities.InputPurchase;
import java.time.LocalDate;

/**
 * DTO para exibição de vendas recentes no widget lateral da listagem.
 */
public record RecentSaleDto(
        Long id,
        String productName,
        LocalDate purchaseDate,
        Double totalValue
) {
    public RecentSaleDto(InputPurchase entity) {
        this(
                entity.getId(),
                extractProductName(entity),
                entity.getEmissionDate() != null ? entity.getEmissionDate().toLocalDate() : null,
                entity.getTotalValue() != null && entity.getTotalValue().getValue() != null
                        ? entity.getTotalValue().getValue().doubleValue()
                        : 0.0
        );
    }

    private static String extractProductName(InputPurchase entity) {
        if (entity.getPurchaseItems() == null || entity.getPurchaseItems().isEmpty()) {
            return "Venda #" + entity.getId();
        }
        var firstItem = entity.getPurchaseItems().getFirst();
        if (firstItem.getInputBatch() != null
                && firstItem.getInputBatch().getInputProduct() != null
                && firstItem.getInputBatch().getInputProduct().getProductName() != null) {
            String name = firstItem.getInputBatch().getInputProduct().getProductName();
            int total = entity.getPurchaseItems().size();
            return total > 1 ? name + " (+" + (total - 1) + ")" : name;
        }
        return "Venda #" + entity.getId();
    }
}

