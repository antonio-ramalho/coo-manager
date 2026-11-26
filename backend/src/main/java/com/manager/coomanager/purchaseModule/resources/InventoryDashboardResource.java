package com.manager.coomanager.purchaseModule.resources;

import com.manager.coomanager.purchaseModule.dto.dashboard.ExpiredBatchDto;
import com.manager.coomanager.purchaseModule.dto.dashboard.ExpiringBatchDto;
import com.manager.coomanager.purchaseModule.dto.dashboard.InventoryDashboardDto;
import com.manager.coomanager.purchaseModule.dto.dashboard.SalesDashboardDto;
import com.manager.coomanager.purchaseModule.services.interfaces.InventoryDashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping(value = "/input-batches/dashboard")
public class InventoryDashboardResource {

    private final InventoryDashboardService dashboardService;

    public InventoryDashboardResource(InventoryDashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('INPUT_READ') or hasAuthority('SALES_READ')")
    public ResponseEntity<InventoryDashboardDto> getDashboardSummary() {
        InventoryDashboardDto dto = dashboardService.getDashboardSummary();
        return ResponseEntity.ok(dto);
    }

    @GetMapping(value = "/expiring")
    @PreAuthorize("hasAuthority('INPUT_READ')")
    public ResponseEntity<List<ExpiringBatchDto>> getExpiringBatches(
            @RequestParam(defaultValue = "3") int days) {
        List<ExpiringBatchDto> list = dashboardService.getExpiringBatches(days);
        return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/expired")
    @PreAuthorize("hasAuthority('INPUT_READ')")
    public ResponseEntity<List<ExpiredBatchDto>> getExpiredBatches(
            @RequestParam(defaultValue = "3") int days) {
        List<ExpiredBatchDto> list = dashboardService.getExpiredBatches(days);
        return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/sales")
    @PreAuthorize("hasAuthority('SALES_READ')")
    public ResponseEntity<SalesDashboardDto> getSalesDashboard() {
        SalesDashboardDto dto = dashboardService.getSalesDashboard();
        return ResponseEntity.ok(dto);
    }
}
