package com.manager.coopafi.personModule.dto.consumerunit;

public record ConsumerUnitUpdateDto(
        String legalName,
        String tradeName,
        String addressEmail,
        String phoneNumber,
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
        String deliveryNumber
) {}


