package com.manager.coomanager.contractModule.dto.farmerItemQuota;

public record FarmerItemQuotaInsertDto(
        Long agriculturalProductId,
        Double maxQuantity
) {
}
