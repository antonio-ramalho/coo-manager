package com.manager.coopafi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentStatus {
    PENDENT("Pendente"),
    CANCELED("Cancelado"),
    COMPLETED("Vencido");

    private final String description;
}
