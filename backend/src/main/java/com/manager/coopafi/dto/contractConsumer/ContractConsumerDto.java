package com.manager.coopafi.dto.contractConsumer;

import com.manager.coopafi.domain.entities.ContractConsumer;

public record ContractConsumerDto(
        Long consumerId,
        String consumerName
) {
    public ContractConsumerDto(ContractConsumer entity) {
        this(
                entity.getConsumer().getId(),
                entity.getConsumer().getJuridicPerson().getLegalName()
        );
    }
}
