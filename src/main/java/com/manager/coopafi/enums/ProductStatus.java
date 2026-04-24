package com.manager.coopafi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductStatus {
    AVAILABLE("Disponível"),
    IN_STOCK("Em Estoque"),
    OUT_OF_STOCK("Esgotado"),
    EXPIRED("Vencido"),
    INACTIVE("Inativo");

    private final String description;
}
