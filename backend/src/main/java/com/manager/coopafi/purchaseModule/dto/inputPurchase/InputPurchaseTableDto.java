package com.manager.coopafi.purchaseModule.dto.inputPurchase;

import com.manager.coopafi.purchaseModule.entities.InputPurchase;
import java.time.LocalDate;

/**
 * DTO para alimentação da listagem de vendas na tabela densa.
 */
public record InputPurchaseTableDto(
                Long id,
                String uuid,
                String clientName,
                LocalDate purchaseDate,
                Double totalValue,
                String paymentStatus,
                String paymentStatusDescription) {
        public InputPurchaseTableDto(InputPurchase entity) {
                this(
                                entity.getId(),
                                entity.getUuid() != null ? entity.getUuid().toString() : null,
                                entity.getPerson() != null ? entity.getPerson().getLegalName() : "Não informado",
                                entity.getEmissionDate() != null ? entity.getEmissionDate().toLocalDate() : null,
                                entity.getTotalValue() != null && entity.getTotalValue().getValue() != null
                                                ? entity.getTotalValue().getValue().doubleValue()
                                                : 0.0,
                                entity.getStatus() != null ? entity.getStatus().name() : "",
                                entity.getStatus() != null ? entity.getStatus().getDescription() : "");
        }
}
