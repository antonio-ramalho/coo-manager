package com.manager.coomanager.purchaseModule.dto.dashboard;

public record TopSellingProductDto(
                Long productId,
                String productName,
                Double totalQuantitySold,
                String measureUnit) {
}
