package com.manager.coopafi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InstitutionSphere {
    PUBLIC("Pública"),
    PRIVATE("Privada");

    private final String description;
}
