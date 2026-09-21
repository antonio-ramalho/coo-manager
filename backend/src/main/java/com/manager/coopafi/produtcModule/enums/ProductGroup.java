package com.manager.coopafi.produtcModule.enums;

import com.manager.coopafi.infrastructure.exceptions.DomainException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductGroup {
    VEGETABLES("Hortaliças"),
    FRUITS("Frutas"),
    SPICES("Temperos");

    private final String description;

    public static ProductGroup validateString(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new DomainException("O grupo não pode estar em branco");
        }

        for (ProductGroup group : ProductGroup.values()) {
            if (group.name().equalsIgnoreCase(value.trim())) {
                return group;
            }
        }

        throw new DomainException("Grupo invalido.");
    }
}
