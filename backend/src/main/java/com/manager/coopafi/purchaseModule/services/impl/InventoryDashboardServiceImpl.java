package com.manager.coopafi.purchaseModule.services.impl;

import com.manager.coopafi.purchaseModule.dto.dashboard.*;
import com.manager.coopafi.purchaseModule.entities.InputBatch;
import com.manager.coopafi.purchaseModule.entities.InputPurchase;
import com.manager.coopafi.purchaseModule.enums.ProductInventoryStatus;
import com.manager.coopafi.purchaseModule.repositories.InputBatchRepository;
import com.manager.coopafi.purchaseModule.repositories.InputPurchaseRepository;
import com.manager.coopafi.purchaseModule.repositories.projections.TopSellingProductProjection;
import com.manager.coopafi.purchaseModule.services.interfaces.InventoryDashboardService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

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

        return new InventoryDashboardDto(totalStockValue, occupancy, expiring);
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

            return new ExpiringBatchDto(
                    batch.getId(),
                    resolveProductName(batch),
                    expDate,
                    daysRemaining,
                    resolveCurrentQuantity(batch));
        }).toList();
    }

    @Override
    @Transactional(readOnly = true)

    public List<ExpiredBatchDto> getExpiredBatches(int days) {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(days);

        List<InputBatch> batches = batchRepository.findExpiredBatches(startDate, today);

        return batches.stream().map(batch -> {
            LocalDate expDate = batch.getExpirationDate().getValue();
            long daysExpired = Math.max(1, ChronoUnit.DAYS.between(expDate, today));

            return new ExpiredBatchDto(
                    batch.getId(),
                    resolveProductName(batch),
                    expDate,
                    daysExpired,
                    resolveCurrentQuantity(batch));
        }).toList();
    }

    private String resolveProductName(InputBatch batch) {
        return batch.getInputProduct() != null ? batch.getInputProduct().getProductName() : "Produto sem nome";
    }

    private Double resolveCurrentQuantity(InputBatch batch) {
        return batch.getCurrentQuantity() != null ? batch.getCurrentQuantity().getAmount().doubleValue() : 0.0;
    }

    private List<TopSellingProductDto> getAbcCurveProducts(int periodDays) {
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

        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public SalesDashboardDto getSalesDashboard() {
        Double averageTicket = calculateAverageTicket();
        Double expirationRiskRate = calculateExpirationRiskRate(90);
        List<MonthlySalesDto> monthlySales = calculateMonthlySales(6);
        List<TopSellingProductDto> topSelling = getAbcCurveProducts(90);

        return new SalesDashboardDto(averageTicket, expirationRiskRate, monthlySales, topSelling);
    }

    private Double calculateAverageTicket() {
        Double avg = purchaseRepository.calculateAverageTicket();
        return avg != null ? BigDecimal.valueOf(avg).setScale(2, RoundingMode.HALF_UP).doubleValue() : 0.0;
    }

    private Double calculateExpirationRiskRate(int days) {
        BigDecimal totalStock = batchRepository.calculateTotalStockValue();
        if (totalStock == null || totalStock.compareTo(BigDecimal.ZERO) <= 0) {
            return 0.0;
        }

        LocalDate endDate = LocalDate.now().plusDays(days);
        BigDecimal expiringStock = batchRepository.calculateExpiringStockValue(endDate);
        if (expiringStock == null || expiringStock.compareTo(BigDecimal.ZERO) <= 0) {
            return 0.0;
        }

        return expiringStock
                .multiply(BigDecimal.valueOf(100))
                .divide(totalStock, 2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    private List<MonthlySalesDto> calculateMonthlySales(int monthsCount) {
        YearMonth currentMonth = YearMonth.now();
        YearMonth startMonth = currentMonth.minusMonths(monthsCount - 1);
        LocalDateTime sinceDate = startMonth.atDay(1).atStartOfDay();

        List<InputPurchase> purchases = purchaseRepository.findPaidPurchasesSince(sinceDate);

        Map<YearMonth, List<InputPurchase>> purchasesByMonth = purchases.stream()
                .filter(p -> p.getEmissionDate() != null)
                .collect(Collectors.groupingBy(p -> YearMonth.from(p.getEmissionDate())));

        List<MonthlySalesDto> result = new ArrayList<>();
        YearMonth cursor = startMonth;
        Locale ptBr = Locale.of("pt", "BR");

        while (!cursor.isAfter(currentMonth)) {
            List<InputPurchase> monthPurchases = purchasesByMonth.getOrDefault(cursor, List.of());

            double totalMonthValue = monthPurchases.stream()
                    .filter(p -> p.getTotalValue() != null && p.getTotalValue().getValue() != null)
                    .mapToDouble(p -> p.getTotalValue().getValue().doubleValue())
                    .sum();

            BigDecimal roundedTotal = BigDecimal.valueOf(totalMonthValue).setScale(2, RoundingMode.HALF_UP);
            String rawMonth = cursor.getMonth().getDisplayName(TextStyle.SHORT, ptBr).replace(".", "");
            String formattedMonth = rawMonth.substring(0, 1).toUpperCase(ptBr) + rawMonth.substring(1);
            String label = formattedMonth + "/" + cursor.getYear();

            result.add(new MonthlySalesDto(
                    cursor.getYear(),
                    cursor.getMonthValue(),
                    label,
                    roundedTotal.doubleValue(),
                    (long) monthPurchases.size()));

            cursor = cursor.plusMonths(1);
        }

        return result;
    }
}
