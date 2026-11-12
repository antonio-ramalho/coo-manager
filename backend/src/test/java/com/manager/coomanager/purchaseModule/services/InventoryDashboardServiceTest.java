package com.manager.coomanager.purchaseModule.services;

import com.manager.coomanager.infrastructure.valueObjects.ExpirationDate;
import com.manager.coomanager.infrastructure.valueObjects.Ncm;
import com.manager.coomanager.infrastructure.valueObjects.Price;
import com.manager.coomanager.infrastructure.valueObjects.Quantity;
import com.manager.coomanager.purchaseModule.entities.InputProduct;
import com.manager.coomanager.produtcModule.enums.MeasureUnit;
import com.manager.coomanager.purchaseModule.dto.dashboard.ExpiredBatchDto;
import com.manager.coomanager.purchaseModule.dto.dashboard.ExpiringBatchDto;
import com.manager.coomanager.purchaseModule.dto.dashboard.InventoryDashboardDto;
import com.manager.coomanager.purchaseModule.dto.dashboard.SalesDashboardDto;
import com.manager.coomanager.purchaseModule.entities.InputBatch;
import com.manager.coomanager.purchaseModule.entities.InputPurchase;
import com.manager.coomanager.personModule.entities.Person;
import com.manager.coomanager.purchaseModule.enums.ProductInventoryStatus;
import com.manager.coomanager.purchaseModule.repositories.InputBatchRepository;
import com.manager.coomanager.purchaseModule.repositories.InputPurchaseRepository;
import com.manager.coomanager.purchaseModule.services.impl.InventoryDashboardServiceImpl;
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
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
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

        InventoryDashboardDto summary = dashboardService.getDashboardSummary();

        assertThat(summary).isNotNull();
        assertThat(summary.totalStockValue()).isEqualByComparingTo(new BigDecimal("10000.00"));

        assertThat(summary.occupancy().totalProducts()).isEqualTo(20L);
        assertThat(summary.occupancy().inStockCount()).isEqualTo(10L);
        assertThat(summary.occupancy().lowStockCount()).isEqualTo(6L);
        assertThat(summary.occupancy().outOfStockCount()).isEqualTo(4L);

        assertThat(summary.expiringBatches()).hasSize(1);
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
    @DisplayName("Deve retornar lotes vencidos dentro da janela informada com dias expirados calculados")
    void shouldReturnExpiredBatchesWithCalculatedExpiredDays() {
        Quantity qty = new Quantity(new BigDecimal("10.00"));
        InputBatch expiredBatch = new InputBatch(qty, testProduct, qty,
                new ExpirationDate(LocalDate.now().plusDays(1)));
        ReflectionTestUtils.setField(expiredBatch, "id", 200L);
        ReflectionTestUtils.setField(expiredBatch.getExpirationDate(), "value", LocalDate.now().minusDays(4));

        when(batchRepository.findExpiredBatches(any(), any())).thenReturn(List.of(expiredBatch));

        List<ExpiredBatchDto> result = dashboardService.getExpiredBatches(7);

        assertThat(result).hasSize(1);
        ExpiredBatchDto dto = result.getFirst();
        assertThat(dto.batchId()).isEqualTo(200L);
        assertThat(dto.productName()).isEqualTo("Adubo Fosfatado");
        assertThat(dto.daysExpired()).isEqualTo(4L);
        assertThat(dto.currentQuantity()).isEqualTo(10.0);
    }

    @Test

    @DisplayName("Deve retornar lista vazia quando não houver lotes vencidos no período")
    void shouldReturnEmptyListWhenNoExpiredBatches() {
        when(batchRepository.findExpiredBatches(any(), any())).thenReturn(Collections.emptyList());

        List<ExpiredBatchDto> result = dashboardService.getExpiredBatches(30);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Deve calcular métricas gerenciais do painel de vendas corretamente")
    void shouldCalculateSalesDashboardMetricsCorrectly() {
        when(purchaseRepository.calculateAverageTicket()).thenReturn(350.50);
        when(batchRepository.calculateTotalStockValue()).thenReturn(new BigDecimal("10000.00"));
        when(batchRepository.calculateExpiringStockValue(any())).thenReturn(new BigDecimal("1500.00"));
        when(purchaseRepository.findTopSellingProducts(any(), any())).thenReturn(Collections.emptyList());

        Person client = mock(Person.class);
        InputPurchase purchase = new InputPurchase(client);
        ReflectionTestUtils.setField(purchase, "totalValue", new Price(new BigDecimal("200.00")));
        ReflectionTestUtils.setField(purchase, "emissionDate", LocalDateTime.now());

        when(purchaseRepository.findPaidPurchasesSince(any())).thenReturn(List.of(purchase));

        SalesDashboardDto salesDashboard = dashboardService.getSalesDashboard();

        assertThat(salesDashboard).isNotNull();
        assertThat(salesDashboard.averageTicket()).isEqualTo(350.50);
        assertThat(salesDashboard.expirationRiskRate()).isEqualTo(15.0);
        assertThat(salesDashboard.monthlySales()).hasSize(6);
        assertThat(salesDashboard.topSellingProducts()).isEmpty();
    }
}
