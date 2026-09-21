package com.manager.coopafi.personModule.enums;

import com.manager.coopafi.infrastructure.exceptions.DomainException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {
    MALE("Masculino"),
    FEMALE("Feminino"),
    OTHER("Outro"),
    NOT_SAY("Prefiro não dizer");

    private final String description;

    public static Gender validateString(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new DomainException("É preciso cadastrar um gênero");
        }

        for (Gender rule : Gender.values()) {
            if (rule.name().equalsIgnoreCase(value.trim())) {
                return rule;
            }
        }

        throw new DomainException("Gênero inválido.");
    }
}
