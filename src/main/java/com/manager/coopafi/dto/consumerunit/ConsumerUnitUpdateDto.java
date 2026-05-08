package com.manager.coopafi.dto.consumerunit;

public record ConsumerUnitUpdateDto(
        String legalName,
        String tradeName,
        String addressEmail,
        String phoneNumber,

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


