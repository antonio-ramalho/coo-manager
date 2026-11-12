package com.manager.coomanager.personModule.dto.consumerunit;

import com.manager.coomanager.personModule.entities.ConsumerUnit;
import com.manager.coomanager.personModule.dto.agent.AgentDto;

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


