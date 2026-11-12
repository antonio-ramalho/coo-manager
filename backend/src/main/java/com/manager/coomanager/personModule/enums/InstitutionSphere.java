package com.manager.coomanager.personModule.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InstitutionSphere {
    PUBLIC("Pública"),
    PRIVATE("Privada");

    private final String description;
}
