package com.manager.coopafi.dto.farmerItemQuota;

import com.manager.coopafi.domain.entities.FarmerItemQuota;

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
