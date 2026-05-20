package com.manager.coopafi.enums;

import com.manager.coopafi.exceptions.DomainException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MeasureUnit {
    KILOGRAM("Kilograma"),
    UNIT("Unitário"),
    LITER("Litro");

    private final String description;

    public static MeasureUnit validateString(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new DomainException("A unidade de medida não pode estar em branco.");
        }

        for (MeasureUnit unit : MeasureUnit.values()) {
            if (unit.name().equalsIgnoreCase(value.trim())) {
                return unit;
            }
        }

        throw new DomainException("Unidade de medida inválida.");
    }
}
