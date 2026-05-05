package com.manager.coopafi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatus {
    ACTIVE("Ativo"),
    INACTIVE("Inativo"),
    SUSPENDED("Suspenso");

    private final String value;
}
