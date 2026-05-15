package com.manager.coopafi.dto.farmerItemQuota;

public record FarmerItemQuotaInsertDto(
        Long agriculturalProductId,
        Double maxQuantity
) {
}
