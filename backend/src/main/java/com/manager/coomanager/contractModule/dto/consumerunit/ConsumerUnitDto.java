package com.manager.coomanager.contractModule.dto.consumerunit;

import com.manager.coomanager.contractModule.entities.ConsumerUnit;
import com.manager.coomanager.contractModule.dto.agent.AgentDto;

import java.util.List;

public record ConsumerUnitDto(
        Long id,
        String legalName,
        String cnpj,
        String deliveryStreet,
        String deliveryCity,
        List <AgentDto> agents,
        boolean isSubsidiaryCNPJ
) {
    public ConsumerUnitDto(ConsumerUnit entity) {
        this(
                entity.getId(),
                entity.getJuridicPerson().getLegalName(),
                entity.getJuridicPerson().getCnpj().getCnpjNumber(),
                entity.getDeliveryAddress().getStreet(),
                entity.getDeliveryAddress().getCity(),
                entity.getAgents().stream().map(AgentDto::new).toList(),
                entity.isSubsidiaryCNPJ()
        );
    }
}


