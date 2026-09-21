package com.manager.coopafi.personModule.dto.agent;

import java.time.LocalDate;

public record AgentInsertDto(
        String agentName,
        String agentCpf,
        LocalDate agentBirthDate,
        String agentEmail,
        String agentPhone,
        String agentCargo,
        String gender
) {
}
