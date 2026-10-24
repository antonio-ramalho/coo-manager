package com.manager.coopafi.purchaseModule.resources;

import com.manager.coopafi.purchaseModule.dto.dashboard.ExpiringBatchDto;
import com.manager.coopafi.purchaseModule.dto.dashboard.InventoryDashboardDto;
import com.manager.coopafi.purchaseModule.dto.dashboard.TopSellingProductDto;
import com.manager.coopafi.purchaseModule.services.interfaces.InventoryDashboardService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping(value = "/input-batch/dashboard")
public class InventoryDashboardResource {

    private final InventoryDashboardService dashboardService;

    public InventoryDashboardResource(InventoryDashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public ResponseEntity<InventoryDashboardDto> getDashboardSummary() {
        InventoryDashboardDto dto = dashboardService.getDashboardSummary();
        return ResponseEntity.ok(dto);
    }

    @GetMapping(value = "/expiring")
    public ResponseEntity<List<ExpiringBatchDto>> getExpiringBatches(
            @RequestParam(defaultValue = "3") int days) {
        List<ExpiringBatchDto> list = dashboardService.getExpiringBatches(days);
        return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/top-selling")
    public ResponseEntity<List<TopSellingProductDto>> getTopSellingProducts(
            @RequestParam(defaultValue = "7") int periodDays) {
        List<TopSellingProductDto> list = dashboardService.getTopSellingProducts(periodDays);
        return ResponseEntity.ok(list);
    }
}
