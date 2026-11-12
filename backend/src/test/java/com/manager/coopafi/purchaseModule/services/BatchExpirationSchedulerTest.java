package com.manager.coopafi.purchaseModule.services;

import com.manager.coopafi.infrastructure.valueObjects.ExpirationDate;
import com.manager.coopafi.infrastructure.valueObjects.Ncm;
import com.manager.coopafi.infrastructure.valueObjects.Price;
import com.manager.coopafi.infrastructure.valueObjects.Quantity;
import com.manager.coopafi.produtcModule.enums.MeasureUnit;
import com.manager.coopafi.purchaseModule.entities.InputBatch;
import com.manager.coopafi.purchaseModule.entities.InputProduct;
import com.manager.coopafi.purchaseModule.enums.ProductInventoryStatus;
import com.manager.coopafi.purchaseModule.events.BatchExpiredEvent;
import com.manager.coopafi.purchaseModule.repositories.InputBatchRepository;
import com.manager.coopafi.purchaseModule.services.scheduled.BatchExpirationScheduler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BatchExpirationSchedulerTest {

    @Mock
    private InputBatchRepository batchRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private BatchExpirationScheduler scheduler;

    @Test
    @DisplayName("Deve marcar lotes vencidos como EXPIRED e publicar BatchExpiredEvent")
    void shouldMarkExpiredBatchesAndPublishEvent() {
        InputProduct product = new InputProduct(
                MeasureUnit.UNIT,
                new Ncm("31010000"),
                "Semente Milho",
                new Price(new BigDecimal("120.00")),
                "SEM-01");
        Quantity qty = new Quantity(new BigDecimal("10.00"));
        InputBatch batch = new InputBatch(qty, product, qty, new ExpirationDate(LocalDate.now().plusDays(1)));
        ReflectionTestUtils.setField(batch, "id", 15L);

        when(batchRepository.findUnmarkedExpiredBatches(any(LocalDate.class))).thenReturn(List.of(batch));

        scheduler.processExpiredBatches();

        assertThat(batch.getProductStatus()).isEqualTo(ProductInventoryStatus.EXPIRED);
        verify(batchRepository).saveAll(List.of(batch));

        ArgumentCaptor<BatchExpiredEvent> eventCaptor = ArgumentCaptor.forClass(BatchExpiredEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        assertThat(eventCaptor.getValue().expiredBatchIds()).containsExactly(15L);
    }

    @Test
    @DisplayName("Não deve salvar nem publicar evento quando não houver lotes vencidos")
    void shouldDoNothingWhenNoUnmarkedExpiredBatches() {
        when(batchRepository.findUnmarkedExpiredBatches(any(LocalDate.class))).thenReturn(Collections.emptyList());

        scheduler.processExpiredBatches();

        verify(batchRepository, never()).saveAll(any());
        verify(eventPublisher, never()).publishEvent(any());
    }
}

