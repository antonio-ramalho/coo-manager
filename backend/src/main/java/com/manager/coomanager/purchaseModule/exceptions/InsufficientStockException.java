package com.manager.coomanager.purchaseModule.exceptions;

import com.manager.coomanager.shared.domain.exceptions.DomainException;

public class InsufficientStockException extends DomainException {

    public InsufficientStockException(String message) {
        super(message);
    }
}
