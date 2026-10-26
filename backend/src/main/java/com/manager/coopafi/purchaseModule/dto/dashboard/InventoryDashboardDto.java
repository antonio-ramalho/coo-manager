package com.manager.coopafi.purchaseModule.dto.dashboard;

import java.math.BigDecimal;
import java.util.List;

public record InventoryDashboardDto(
                BigDecimal totalStockValue,
                StockOccupancyDto occupancy,
                List<ExpiringBatchDto> expiringBatches,
                List<TopSellingProductDto> topSellingProducts) {
}
