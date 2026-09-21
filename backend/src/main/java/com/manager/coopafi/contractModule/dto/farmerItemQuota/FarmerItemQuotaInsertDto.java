package com.manager.coopafi.contractModule.dto.farmerItemQuota;

public record FarmerItemQuotaInsertDto(
        Long agriculturalProductId,
        Double maxQuantity
) {
}
