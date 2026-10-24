package com.manager.coopafi.purchaseModule.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductInventoryStatus {
    IN_STOCK("Em Estoque"),
    LOW_STOCK("Estoque Baixo"),
    OUT_OF_STOCK("Esgotado"),
    EXPIRED("Vencido"),
    INACTIVE("Desativado");

    private final String description;
}
