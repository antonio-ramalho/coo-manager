package com.manager.coopafi.purchaseModule.dto.dashboard;

public record StockOccupancyDto(
                long totalProducts,
                long inStockCount,
                long lowStockCount,
                long outOfStockCount) {
}
