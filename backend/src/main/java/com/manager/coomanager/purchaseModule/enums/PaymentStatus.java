package com.manager.coomanager.purchaseModule.enums;

import com.manager.coomanager.infrastructure.exceptions.DomainException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentStatus {
    PAID("Pago"),
    CANCELED("Cancelado"),
    COMPLETED("Vencido"),
    PENDING("Pendente");

    private final String description;

    public boolean canBeModified() {
        return this == PENDING;
    }

    public void validateCanModify() {
        if (!canBeModified()) {
            throw new DomainException("Não é possível alterar uma compra cancelada ou paga.");
        }
    }

    public boolean canTransitionTo(PaymentStatus nextStatus) {
        if (this == PENDING) {
            return nextStatus == PAID || nextStatus == CANCELED || nextStatus == COMPLETED;
        }
        return false;
    }

    public void validateCanTransitionTo(PaymentStatus nextStatus) {
        if (!canTransitionTo(nextStatus)) {
            throw new DomainException(String.format("Transição de status inválida de %s para %s.", this.description,
                    nextStatus.getDescription()));
        }
    }
}
