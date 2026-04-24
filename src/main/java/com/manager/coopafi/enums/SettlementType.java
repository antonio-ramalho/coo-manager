package com.manager.coopafi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SettlementType {
    REGULAR("Agricultor Familiar Regular"),
    SETTLED("Assentado da Reforma Agrária"),
    QUILOMBOLA("Comunidade Quilombola"),
    INDIGENOUS("Povos Indígenas"),
    FISHERMAN("Pescador Artesanal"),
    EXTRACTIVIST("Extrativista");

    private final String description;
}
