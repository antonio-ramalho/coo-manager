package com.manager.coopafi.personModule.dto.consumerunit;

import com.manager.coopafi.personModule.dto.agent.AgentInsertDto;
import java.time.LocalDate;
import java.util.List;

public record ConsumerUnitInsertDto (

        String legalName,
        String tradeName,
        String cnpj,
        String addressEmail,
        String phoneNumber,
        LocalDate birthDate,
        boolean isSubsidiaryCNPJ,

        String cepNumber,
        String street,
        String neighborhood,
        String city,
        String addressNumber,

        String deliveryCep,
        String deliveryStreet,
        String deliveryNeighborhood,
        String deliveryCity,
        String deliveryNumber,

        List<AgentInsertDto> agents
) {}


