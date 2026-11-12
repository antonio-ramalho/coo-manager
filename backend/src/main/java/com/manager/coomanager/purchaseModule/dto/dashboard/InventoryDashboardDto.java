package com.manager.coomanager.purchaseModule.dto.dashboard;

import java.math.BigDecimal;
import java.util.List;

public record InventoryDashboardDto(
                BigDecimal totalStockValue,
                StockOccupancyDto occupancy,
                List<ExpiringBatchDto> expiringBatches) {
}
