package com.manager.coopafi.dto.agent;

import java.time.LocalDate;

public record AgentInsertDto(
        String agentName,
        String agentCpf,
        LocalDate agentBirthDate,
        String agentEmail,
        String agentPhone,
        String agentCargo
) {
}
