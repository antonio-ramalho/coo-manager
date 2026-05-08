package com.manager.coopafi.dto.agent;

public record AgentUpdateDto(
        String agentName,
        String agentPhone,
        String agentEmail,
        String agentCargo
) {
}
