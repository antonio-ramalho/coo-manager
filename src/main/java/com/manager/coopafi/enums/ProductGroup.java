package com.manager.coopafi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductGroup {
    VEGETABLES("Hortaliças"),
    FRUITS("Frutas"),
    SPICES("Temperos");

    private final String description;
}
