package com.manager.coopafi.personModule.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    ACTIVE("Ativo"),
    INACTIVE("Inativo"),
    SUSPENDED("Suspenso");

    private final String value;

    public Status activate() {
        return ACTIVE;
    }

    public Status suspend() {
        return SUSPENDED;
    }

    public Status deactivate() {
        return INACTIVE;
    }
}

