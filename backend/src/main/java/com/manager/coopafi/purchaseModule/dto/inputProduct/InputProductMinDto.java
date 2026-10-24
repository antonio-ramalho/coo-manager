package com.manager.coopafi.purchaseModule.dto.inputProduct;

import com.manager.coopafi.produtcModule.entities.InputProduct;

public record InputProductMinDto(
        Long id,
        String productName,
        String productCode,
        String productCatalogStatus) {
    public InputProductMinDto(InputProduct entity) {
        this(
                entity.getId(),
                entity.getProductName(),
                entity.getProductCode(),
                entity.getProductCatalogStatus() != null ? entity.getProductCatalogStatus().name() : null);
    }
}
