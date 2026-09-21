package com.manager.coopafi.personModule.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PersonType {
    NATURAL("Pessoa Física"),
    JURIDIC("Pessoa Jurídica");

    private final String description;
}