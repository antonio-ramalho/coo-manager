package com.manager.coomanager.purchaseModule.services.interfaces;

import com.manager.coomanager.purchaseModule.dto.dashboard.ExpiredBatchDto;
import com.manager.coomanager.purchaseModule.dto.dashboard.ExpiringBatchDto;
import com.manager.coomanager.purchaseModule.dto.dashboard.InventoryDashboardDto;
import com.manager.coomanager.purchaseModule.dto.dashboard.SalesDashboardDto;
import java.util.List;

public interface InventoryDashboardService {

    InventoryDashboardDto getDashboardSummary();

    List<ExpiringBatchDto> getExpiringBatches(int days);


    List<ExpiredBatchDto> getExpiredBatches(int days);

    SalesDashboardDto getSalesDashboard();
}
