package com.manager.coopafi.dto.inputPurchase;

import com.manager.coopafi.domain.entities.InputPurchase;
import com.manager.coopafi.dto.inputPurchaseItem.InputPurchaseItemDto;
import java.time.LocalDate;
import java.util.List;

public record InputPurchaseDto(
        Long id,
        String farmerName,
        Double totalValue,
        LocalDate purchaseDate,
        String paymentStatus,
        List<InputPurchaseItemDto> items
) {
    public InputPurchaseDto(InputPurchase entity) {
        this(
                entity.getId(),
                entity.getFarmer().getPerson().getName(),
                entity.getTotalValue().getValue().doubleValue(),
                entity.getEmissionDate().toLocalDate(),
                entity.getStatus().getDescription(),

                entity.getPurchaseItems().stream()
                        .map(InputPurchaseItemDto::new)
                        .toList()
        );
    }
}
