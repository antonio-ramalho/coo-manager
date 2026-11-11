package com.manager.coopafi.purchaseModule.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Ouvinte de eventos responsável por reagir à expiração automática de lotes.
 * Preparado para disparar notificações aos usuários quando o módulo de autenticação/alertas for integrado.
 */
@Component
public class BatchExpirationEventListener {

    private static final Logger log = LoggerFactory.getLogger(BatchExpirationEventListener.class);

    @EventListener
    public void onBatchesExpired(BatchExpiredEvent event) {
        log.info("Notificação de vencimento: {} lote(s) marcado(s) como EXPIRED na data de referência {}. IDs: {}",
                event.expiredBatchIds().size(),
                event.referenceDate(),
                event.expiredBatchIds());
    }
}

