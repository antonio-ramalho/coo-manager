package com.manager.coopafi.enums;

import com.manager.coopafi.exceptions.DomainException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductDeliveryRule {
    VALUE_ONLY("Somente por valor."),
    QUANTITY_LOCKED("Por quantidade de produto."),
    GROUP_LOCKED("Por quantidade de grupo.");

    private final String description;

    public static ProductDeliveryRule validateString(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new DomainException("É preciso cadastrar uma regra de entrega.");
        }

        for (ProductDeliveryRule rule : ProductDeliveryRule.values()) {
            if (rule.name().equalsIgnoreCase(value.trim())) {
                return rule;
            }
        }

        throw new DomainException("Regra de entrega invalida.");
    }
}
