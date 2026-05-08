package com.manager.coopafi.dto.agent;

import com.manager.coopafi.domain.entities.Agent;

public record AgentDto(
        Long id,
        String name,
        String cargo,
        String email
) {
    public AgentDto(Agent entity) {
        this(
                entity.getId(),
                entity.getNatPerson().getName(),
                entity.getCargo(),
                entity.getNatPerson().getEmail().getAddressEmail()
        );
    }
}


