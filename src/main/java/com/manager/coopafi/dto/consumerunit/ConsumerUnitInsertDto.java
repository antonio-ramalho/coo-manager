package com.manager.coopafi.dto.consumerunit;

import com.manager.coopafi.dto.agent.AgentInsertDto;
import java.time.LocalDate;
import java.util.List;

public record ConsumerUnitInsertDto (
        // 1. Dados da Instituição (JuridicPerson)
        String legalName,
        String tradeName,
        String cnpj,
        String addressEmail,
        String phoneNumber,
        LocalDate birthDate,

        // 2. Endereço Principal (Sede da Instituição)
        String cepNumber,
        String street,
        String neighborhood,
        String city,
        String addressNumber,

        // 3. Endereço de Entrega (Específico da ConsumerUnit)
        String deliveryCep,
        String deliveryStreet,
        String deliveryNeighborhood,
        String deliveryCity,
        String deliveryNumber,

        List<AgentInsertDto> agents
) {}


