package com.manager.coomanager.contractModule.dto.agent;

public record AgentUpdateDto(
        String agentName,
        String agentPhone,
        String agentEmail,
        String agentCargo,
        String gender
) {
}
