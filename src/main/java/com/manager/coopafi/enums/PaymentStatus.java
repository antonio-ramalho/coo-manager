package com.manager.coopafi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentStatus {
    PAID("Pendente"),
    CANCELED("Cancelado"),
    COMPLETED("Vencido");

    private final String description;
}
