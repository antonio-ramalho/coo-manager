package com.manager.coopafi.contractModule.dto.contractConsumer;

import com.manager.coopafi.contractModule.entities.ContractConsumer;

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
