package com.manager.coopafi.purchaseModule.services.interfaces;

import com.manager.coopafi.purchaseModule.dto.dashboard.ExpiringBatchDto;
import com.manager.coopafi.purchaseModule.dto.dashboard.InventoryDashboardDto;
import com.manager.coopafi.purchaseModule.dto.dashboard.TopSellingProductDto;
import java.util.List;

public interface InventoryDashboardService {

    InventoryDashboardDto getDashboardSummary();

    List<ExpiringBatchDto> getExpiringBatches(int days);

    List<TopSellingProductDto> getTopSellingProducts(int periodDays);
}
