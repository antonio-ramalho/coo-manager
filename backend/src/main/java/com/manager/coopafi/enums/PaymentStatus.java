package com.manager.coopafi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentStatus {
    PAID("Pago"),
    CANCELED("Cancelado"),
    COMPLETED("Vencido"),
    PENDING("Pendente"),;

    private final String description;
}
