package com.manager.coomanager.purchaseModule.dto.inputPurchase;

import com.manager.coomanager.purchaseModule.dto.inputPurchaseItem.InputPurchaseItemDto;
import com.manager.coomanager.purchaseModule.entities.InputPurchase;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO completo para visualização detalhada e retorno de transações de venda.
 */
public record InputPurchaseDto(
        Long id,
        String uuid,
        String clientName,
        String clientDocument,
        LocalDate clientBirthDate,
        String clientStatus,
        Double totalValue,
        LocalDate purchaseDate,
        String paymentStatus,
        String paymentStatusDescription,
        List<InputPurchaseItemDto> items
) {
    public InputPurchaseDto(InputPurchase entity) {
        this(
                entity.getId(),
                entity.getUuid() != null ? entity.getUuid().toString() : null,
                entity.getPerson() != null ? entity.getPerson().getLegalName() : "Não informado",
                entity.getPerson() != null ? entity.getPerson().getDocumentNumber() : null,
                entity.getPerson() != null ? entity.getPerson().resolveBirthDate() : null,
                entity.getPerson() != null && entity.getPerson().getStatus() != null
                        ? entity.getPerson().getStatus().name()
                        : "",
                entity.getTotalValue() != null && entity.getTotalValue().getValue() != null
                        ? entity.getTotalValue().getValue().doubleValue()
                        : 0.0,
                entity.getEmissionDate() != null ? entity.getEmissionDate().toLocalDate() : null,
                entity.getStatus() != null ? entity.getStatus().name() : "",
                entity.getStatus() != null ? entity.getStatus().getDescription() : "",
                entity.getPurchaseItems().stream()
                        .map(InputPurchaseItemDto::new)
                        .toList()
        );
    }
}
