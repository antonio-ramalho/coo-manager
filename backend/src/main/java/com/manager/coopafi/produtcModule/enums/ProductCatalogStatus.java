package com.manager.coopafi.produtcModule.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductCatalogStatus {
    ACTIVE("Ativo") {
        @Override
        public boolean canReceiveBatches() {
            return true;
        }
    },
    INACTIVE("Inativo") {
        @Override
        public boolean canReceiveBatches() {
            return false;
        }
    },
    DEVELOPMENT("Em Desenvolvimento") {
        @Override
        public boolean canReceiveBatches() {
            return false;
        }
    };

    private final String description;

    public abstract boolean canReceiveBatches();
}
