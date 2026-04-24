package com.manager.coopafi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MeasureUnit {
    KILOGRAM("Kilograma"),
    UNIT("Unitário"),
    LITER("Litro");

    private final String description;
}
