package com.manager.coopafi.purchaseModule.dto.inputPurchase;

import com.manager.coopafi.purchaseModule.entities.InputPurchase;

import java.time.LocalDate;

public record InputPurchaseMinDto (
        Long id,
        String clientName,
        Double totalValue,
        LocalDate purchaseDate,
        String paymentStatus
) {
    public InputPurchaseMinDto(InputPurchase entity) {
        this(entity.getId(), entity.getPerson().getLegalName(),
                entity.getTotalValue().getValue().doubleValue(),
                entity.getEmissionDate().toLocalDate(),
                entity.getStatus().getDescription());
    }
}
