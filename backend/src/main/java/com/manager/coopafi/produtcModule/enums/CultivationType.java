package com.manager.coopafi.produtcModule.enums;

import com.manager.coopafi.infrastructure.exceptions.DomainException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CultivationType {
    ORGANIC("Orgânico"),
    CONVENTIONAL("Convencional");

    private final String description;

    public static CultivationType validateString(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new DomainException("O tipo não pode estar em branco.");
        }

        for (CultivationType type : CultivationType.values()) {
            if (type.name().equalsIgnoreCase(value.trim())) {
                return type;
            }
        }

        throw new DomainException("Tipo invalido.");
    }
}
