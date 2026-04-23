package com.manager.coopafi.enums;

public enum SettlementType {
    REGULAR("Agricultor Familiar Regular"),
    SETTLED("Assentado da Reforma Agrária"),
    QUILOMBOLA("Comunidade Quilombola"),
    INDIGENOUS("Povos Indígenas"),
    FISHERMAN("Pescador Artesanal"),
    EXTRACTIVIST("Extrativista");

    private final String description;

    SettlementType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
