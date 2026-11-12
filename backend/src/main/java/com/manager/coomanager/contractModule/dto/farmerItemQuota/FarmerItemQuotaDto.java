package com.manager.coomanager.contractModule.dto.farmerItemQuota;

import com.manager.coomanager.contractModule.entities.FarmerItemQuota;

public record FarmerItemQuotaDto(
        Long agriculturalProductId,
        String productName,
        Double maxQuantity,
        Double deliveryQuantity
) {
    public FarmerItemQuotaDto(FarmerItemQuota entity) {
        this(
                entity.getContractedProduct().getProduct().getId(),
                entity.getContractedProduct().getContractedName(),
                entity.getMaxQuantity().getAmount().doubleValue(),
                entity.getDeliveryQuantity().getAmount().doubleValue()
        );
    }
}
