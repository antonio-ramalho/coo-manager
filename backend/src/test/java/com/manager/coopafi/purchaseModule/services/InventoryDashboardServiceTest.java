package com.manager.coopafi.purchaseModule.services;

import com.manager.coopafi.infrastructure.valueObjects.ExpirationDate;
import com.manager.coopafi.infrastructure.valueObjects.Ncm;
import com.manager.coopafi.infrastructure.valueObjects.Price;
import com.manager.coopafi.infrastructure.valueObjects.Quantity;
import com.manager.coopafi.purchaseModule.entities.InputProduct;
import com.manager.coopafi.produtcModule.enums.MeasureUnit;
import com.manager.coopafi.purchaseModule.dto.dashboard.ExpiringBatchDto;
import com.manager.coopafi.purchaseModule.dto.dashboard.InventoryDashboardDto;
import com.manager.coopafi.purchaseModule.dto.dashboard.TopSellingProductDto;
import com.manager.coopafi.purchaseModule.entities.InputBatch;
import com.manager.coopafi.purchaseModule.enums.ProductInventoryStatus;
import com.manager.coopafi.purchaseModule.repositories.InputBatchRepository;
import com.manager.coopafi.purchaseModule.repositories.InputPurchaseRepository;
import com.manager.coopafi.purchaseModule.repositories.projections.TopSellingProductProjection;
import com.manager.coopafi.purchaseModule.services.impl.InventoryDashboardServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryDashboardServiceTest {

    @Mock
    private InputBatchRepository batchRepository;

    @Mock
    private InputPurchaseRepository purchaseRepository;

    @InjectMocks
    private InventoryDashboardServiceImpl dashboardService;

    private InputProduct testProduct;
    private InputBatch expiringBatch;

    @BeforeEach
    void setUp() {
        testProduct = new InputProduct(
                MeasureUnit.KILOGRAM,
                new Ncm("31010000"),
                "Adubo Fosfatado",
                new Price(new BigDecimal("500.00")),
                "ADUBO-01");
        ReflectionTestUtils.setField(testProduct, "id", 1L);

        Quantity qty = new Quantity(new BigDecimal("20.00"));
        expiringBatch = new InputBatch(qty, testProduct, qty, new ExpirationDate(LocalDate.now().plusDays(2)));
        ReflectionTestUtils.setField(expiringBatch, "id", 100L);
    }

    @Test
    @DisplayName("Deve consolidar métricas de BI e status de ocupação no getDashboardSummary")
    void shouldConsolidateDashboardSummaryCorrectly() {
        when(batchRepository.calculateTotalStockValue()).thenReturn(new BigDecimal("10000.00"));
        when(batchRepository.countByProductStatus(ProductInventoryStatus.IN_STOCK)).thenReturn(10L);
        when(batchRepository.countByProductStatus(ProductInventoryStatus.LOW_STOCK)).thenReturn(6L);
        when(batchRepository.countByProductStatus(ProductInventoryStatus.OUT_OF_STOCK)).thenReturn(4L);
        when(batchRepository.findExpiringBatches(any(), any())).thenReturn(List.of(expiringBatch));
        when(purchaseRepository.findTopSellingProducts(any(), any())).thenReturn(Collections.emptyList());

        InventoryDashboardDto summary = dashboardService.getDashboardSummary();

        assertThat(summary).isNotNull();
        assertThat(summary.totalStockValue()).isEqualByComparingTo(new BigDecimal("10000.00"));

        assertThat(summary.occupancy().totalProducts()).isEqualTo(20L);
        assertThat(summary.occupancy().inStockCount()).isEqualTo(10L);
        assertThat(summary.occupancy().lowStockCount()).isEqualTo(6L);
        assertThat(summary.occupancy().outOfStockCount()).isEqualTo(4L);

        assertThat(summary.expiringBatches()).hasSize(1);
        assertThat(summary.topSellingProducts()).isNotEmpty();
    }

    @Test
    @DisplayName("Deve retornar lotes próximos do vencimento com dias restantes calculados")
    void shouldReturnExpiringBatchesWithCalculatedRemainingDays() {
        when(batchRepository.findExpiringBatches(any(), any())).thenReturn(List.of(expiringBatch));

        List<ExpiringBatchDto> result = dashboardService.getExpiringBatches(3);

        assertThat(result).hasSize(1);
        ExpiringBatchDto dto = result.getFirst();
        assertThat(dto.productName()).isEqualTo("Adubo Fosfatado");
        assertThat(dto.batchId()).isEqualTo(100L);
        assertThat(dto.daysRemaining()).isLessThanOrEqualTo(2L);
        assertThat(dto.currentQuantity()).isEqualTo(20.0);
    }

    @Test
    @DisplayName("Deve mapear projeção de produtos mais vendidos quando houver registros de vendas")
    void shouldMapTopSellingProductsFromProjectionWhenAvailable() {
        TopSellingProductProjection projection = new TopSellingProductProjection() {
            @Override
            public Long getProductId() {
                return 50L;
            }

            @Override
            public String getProductName() {
                return "Biofertilizante Foliar";
            }

            @Override
            public Double getTotalQuantitySold() {
                return 150.0;
            }

            @Override
            public String getMeasureUnit() {
                return "LITER";
            }
        };

        when(purchaseRepository.findTopSellingProducts(any(), any())).thenReturn(List.of(projection));

        List<TopSellingProductDto> result = dashboardService.getTopSellingProducts(30);

        assertThat(result).hasSize(1);
        TopSellingProductDto dto = result.getFirst();
        assertThat(dto.productId()).isEqualTo(50L);
        assertThat(dto.productName()).isEqualTo("Biofertilizante Foliar");
        assertThat(dto.totalQuantitySold()).isEqualTo(150.0);
        assertThat(dto.measureUnit()).isEqualTo("LITER");
    }

    @Test
    @DisplayName("Deve retornar ranking mockado consistente quando não houver vendas no período")
    void shouldReturnMockTopSellingProductsWhenNoSales() {
        when(purchaseRepository.findTopSellingProducts(any(), any())).thenReturn(Collections.emptyList());

        List<TopSellingProductDto> result = dashboardService.getTopSellingProducts(7);

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(5);
        assertThat(result.getFirst().productName()).isEqualTo("Adubo Orgânico Fosfatado");
    }
}
