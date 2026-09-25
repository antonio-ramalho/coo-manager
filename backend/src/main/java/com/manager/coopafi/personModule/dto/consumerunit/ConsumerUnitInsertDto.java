package com.manager.coopafi.personModule.dto.consumerunit;

import com.manager.coopafi.infrastructure.valueObjectsDto.AddressDto;
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
        AddressDto address,
        AddressDto deliveryAddress,
        List<AgentInsertDto> agents
) {}


