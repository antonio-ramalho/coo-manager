package com.manager.coomanager.contractModule.dto.contractConsumer;

import com.manager.coomanager.contractModule.entities.ContractConsumer;

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
