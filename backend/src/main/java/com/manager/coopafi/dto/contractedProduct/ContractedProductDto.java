package com.manager.coopafi.dto.contractedProduct;

import com.manager.coopafi.domain.entities.ContractedProduct;

public record ContractedProductDto(
        Long contractedProductId,
        String productName,
        Double fixedPrice,
        Long agriculturalProductId,
        Double quantity
) {
    public ContractedProductDto(ContractedProduct entity) {
        this(
                entity.getId(),
                entity.getContractedName(),
                entity.getFixedPrice().getValue().doubleValue(),
                entity.getProduct().getId(),
                entity.getQuantity().getAmount().doubleValue()
        );
    }
}
