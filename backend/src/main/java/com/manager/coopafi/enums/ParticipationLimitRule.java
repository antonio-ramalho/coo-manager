package com.manager.coopafi.enums;

import com.manager.coopafi.exceptions.DomainException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ParticipationLimitRule {
    PER_CPF("Por CAF"),
    PER_CAF("Por CPF"),
    UNLIMITED("Sem limite");

    private final String description;

    public static ParticipationLimitRule fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new DomainException("É preciso cadastrar uma regra de pagamento.");
        }

        for (ParticipationLimitRule rule : ParticipationLimitRule.values()) {
            if (rule.name().equalsIgnoreCase(value.trim())) {
                return rule;
            }
        }

        throw new DomainException("Regra de pagamento invalida.");
    }
}


