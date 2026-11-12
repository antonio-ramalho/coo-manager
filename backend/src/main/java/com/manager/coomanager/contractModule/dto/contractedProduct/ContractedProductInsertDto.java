package com.manager.coomanager.contractModule.dto.contractedProduct;

public record ContractedProductInsertDto(
        Long agriculturalProductId,
        String productName,
        Double fixedPrice,
        Double quantity
) {
}
