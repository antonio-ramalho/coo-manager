package com.manager.coopafi.personModule.dto.agent;

import com.manager.coopafi.personModule.entities.Agent;

public record AgentDto(
        Long id,
        String name,
        String cargo,
        String email,
        String gender
) {
    public AgentDto(Agent entity) {
        this(
                entity.getId(),
                entity.getNatPerson().getLegalName(),
                entity.getCargo(),
                entity.getNatPerson().getEmail().getAddressEmail(),
                entity.getNatPerson().getGender().getDescription()
        );
    }
}


