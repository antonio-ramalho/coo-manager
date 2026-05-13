package com.manager.coopafi.dto.inputPurchase;

import com.manager.coopafi.domain.entities.InputPurchase;

import java.time.LocalDate;

public record InputPurchaseMinDto (
        Long id,
        String farmerName,
        Double totalValue,
        LocalDate purchaseDate,
        String paymentStatus
) {
    public InputPurchaseMinDto(InputPurchase entity) {
        this(entity.getId(), entity.getFarmer().getPerson().getName(),
                entity.getTotalValue().getValue().doubleValue(),
                entity.getEmissionDate().toLocalDate(),
                entity.getStatus().getDescription());
    }
}
