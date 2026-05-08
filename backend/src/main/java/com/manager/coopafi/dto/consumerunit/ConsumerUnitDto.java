package com.manager.coopafi.dto.consumerunit;

import com.manager.coopafi.domain.entities.ConsumerUnit;
import com.manager.coopafi.dto.agent.AgentDto;

import java.util.List;

public record ConsumerUnitDto(
        Long id,
        String legalName,
        String cnpj,
        String deliveryStreet,
        String deliveryCity,
        List <AgentDto> agents
) {
    public ConsumerUnitDto(ConsumerUnit entity) {
        this(
                entity.getId(),
                entity.getJuridicPerson().getLegalName(),
                entity.getJuridicPerson().getCnpj().getCnpjNumber(),
                entity.getDeliveryAddress().getStreet(),
                entity.getDeliveryAddress().getCity(),
                entity.getAgents().stream().map(AgentDto::new).toList()
        );
    }
}


