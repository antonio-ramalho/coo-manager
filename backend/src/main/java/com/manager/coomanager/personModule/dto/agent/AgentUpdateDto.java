package com.manager.coomanager.personModule.dto.agent;

public record AgentUpdateDto(
        String agentName,
        String agentPhone,
        String agentEmail,
        String agentCargo,
        String gender
) {
}
