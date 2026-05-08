package com.manager.coopafi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductInventoryStatus {
    IN_STOCK("Em Estoque"),
    OUT_OF_STOCK("Esgotado"),
    EXPIRED("Vencido");

    private final String description;
}
