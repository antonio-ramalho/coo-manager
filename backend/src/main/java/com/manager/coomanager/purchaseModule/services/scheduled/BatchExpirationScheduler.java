package com.manager.coomanager.purchaseModule.services.scheduled;

import com.manager.coomanager.purchaseModule.entities.InputBatch;
import com.manager.coomanager.purchaseModule.events.BatchExpiredEvent;
import com.manager.coomanager.purchaseModule.repositories.InputBatchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
public class BatchExpirationScheduler {

    private static final Logger log = LoggerFactory.getLogger(BatchExpirationScheduler.class);

    private final InputBatchRepository batchRepository;
    private final ApplicationEventPublisher eventPublisher;

    public BatchExpirationScheduler(InputBatchRepository batchRepository, ApplicationEventPublisher eventPublisher) {
        this.batchRepository = batchRepository;
        this.eventPublisher = eventPublisher;
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void processExpiredBatches() {
        LocalDate today = LocalDate.now();
        List<InputBatch> unmarkedExpiredBatches = batchRepository.findUnmarkedExpiredBatches(today);

        if (unmarkedExpiredBatches.isEmpty()) {
            return;
        }

        for (InputBatch batch : unmarkedExpiredBatches) {
            batch.deactivateByExpirationDate();
        }

        batchRepository.saveAll(unmarkedExpiredBatches);

        List<Long> expiredIds = unmarkedExpiredBatches.stream()
                .map(InputBatch::getId)
                .toList();

        log.info("Scheduler de validade: {} lote(s) marcado(s) como EXPIRED.", expiredIds.size());
        eventPublisher.publishEvent(new BatchExpiredEvent(today, expiredIds));
    }
}
