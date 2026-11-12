package com.manager.coomanager.personModule.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DocumentStatus {
    ACTIVE("Ativo"),
    EXPIRED("Vencido"),
    SUSPENDED("Suspenso");

    private final String description;
}
