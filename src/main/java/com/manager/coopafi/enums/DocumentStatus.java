package com.manager.coopafi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DocumentStatus {
    ACTIVE("Ativo"),
    EXPIRED("Vencido"),
    SUSPENDED("Suspenso"),
    INACTIVE("Inativo");

    private final String description;
}
