package com.manager.coopafi.dto.contractedProduct;

public record ContractedProductInsertDto(
        Long agriculturalProductId,
        String productName,
        Double fixedPrice
) {
}
