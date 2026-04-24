package com.manager.coopafi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductCatalogStatus {
    ACTIVE("Ativo"),
    INACTIVE("Inativo"),
    DEVELOPMENT("Em Desenvolvimento");

    private final String description;
}
