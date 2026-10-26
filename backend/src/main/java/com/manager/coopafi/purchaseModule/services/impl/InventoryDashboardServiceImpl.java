package com.manager.coopafi.purchaseModule.services.impl;

import com.manager.coopafi.purchaseModule.dto.dashboard.ExpiringBatchDto;
import com.manager.coopafi.purchaseModule.dto.dashboard.InventoryDashboardDto;
import com.manager.coopafi.purchaseModule.dto.dashboard.StockOccupancyDto;
import com.manager.coopafi.purchaseModule.dto.dashboard.TopSellingProductDto;
import com.manager.coopafi.purchaseModule.entities.InputBatch;
import com.manager.coopafi.purchaseModule.enums.ProductInventoryStatus;
import com.manager.coopafi.purchaseModule.repositories.InputBatchRepository;
import com.manager.coopafi.purchaseModule.repositories.InputPurchaseRepository;
import com.manager.coopafi.purchaseModule.repositories.projections.TopSellingProductProjection;
import com.manager.coopafi.purchaseModule.services.interfaces.InventoryDashboardService;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class InventoryDashboardServiceImpl implements InventoryDashboardService {

    private final InputBatchRepository batchRepository;

    private final InputPurchaseRepository purchaseRepository;

    public InventoryDashboardServiceImpl(InputBatchRepository batchRepository,
            InputPurchaseRepository purchaseRepository) {
        this.batchRepository = batchRepository;
        this.purchaseRepository = purchaseRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryDashboardDto getDashboardSummary() {
        BigDecimal totalStockValue = batchRepository.calculateTotalStockValue();
        if (totalStockValue == null) {
            totalStockValue = BigDecimal.ZERO;
        }

        long inStock = batchRepository.countByProductStatus(ProductInventoryStatus.IN_STOCK);
        long lowStock = batchRepository.countByProductStatus(ProductInventoryStatus.LOW_STOCK);
        long outOfStock = batchRepository.countByProductStatus(ProductInventoryStatus.OUT_OF_STOCK);
        long totalProducts = inStock + lowStock + outOfStock;

        StockOccupancyDto occupancy = new StockOccupancyDto(totalProducts, inStock, lowStock, outOfStock);
        List<ExpiringBatchDto> expiring = getExpiringBatches(3);
        List<TopSellingProductDto> topSelling = getTopSellingProducts(7);

        return new InventoryDashboardDto(totalStockValue, occupancy, expiring, topSelling);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpiringBatchDto> getExpiringBatches(int days) {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(days);

        List<InputBatch> batches = batchRepository.findExpiringBatches(startDate, endDate);

        return batches.stream().map(batch -> {
            LocalDate expDate = batch.getExpirationDate().getValue();
            long daysRemaining = Math.max(0, ChronoUnit.DAYS.between(startDate, expDate));
            String productName = batch.getInputProduct() != null ? batch.getInputProduct().getProductName()
                    : "Produto sem nome";
            Double quantity = batch.getCurrentQuantity() != null ? batch.getCurrentQuantity().getAmount().doubleValue()
                    : 0.0;

            return new ExpiringBatchDto(
                    batch.getId(),
                    productName,
                    expDate,
                    daysRemaining,
                    quantity);
        }).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TopSellingProductDto> getTopSellingProducts(int periodDays) {
        LocalDateTime sinceDate = LocalDateTime.now().minusDays(periodDays);
        List<TopSellingProductProjection> projections = purchaseRepository.findTopSellingProducts(sinceDate,
                PageRequest.of(0, 5));

        if (projections != null && !projections.isEmpty()) {
            return projections.stream()
                    .map(p -> new TopSellingProductDto(
                            p.getProductId(),
                            p.getProductName(),
                            p.getTotalQuantitySold(),
                            p.getMeasureUnit()))
                    .toList();
        }

        // Mock para demonstração inicial quando não houver compras registradas no
        // período
        return getMockTopSellingProducts(periodDays);
    }

    private List<TopSellingProductDto> getMockTopSellingProducts(int periodDays) {
        List<TopSellingProductDto> mocks = new ArrayList<>();
        double factor = periodDays == 365 ? 12.0 : (periodDays == 30 ? 3.0 : 1.0);

        mocks.add(new TopSellingProductDto(1L, "Adubo Orgânico Fosfatado", 45.0 * factor, "KILOGRAM"));
        mocks.add(new TopSellingProductDto(2L, "Calcário Agrícola Calcitico", 30.0 * factor, "KILOGRAM"));
        mocks.add(new TopSellingProductDto(3L, "Biofertilizante Foliar NPK", 18.0 * factor, "LITER"));
        mocks.add(new TopSellingProductDto(4L, "Semente de Milho Crioulo", 12.0 * factor, "KILOGRAM"));
        mocks.add(new TopSellingProductDto(5L, "Substrato para Mudas", 8.0 * factor, "UNIT"));

        return mocks;
    }
}
