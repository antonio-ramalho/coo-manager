package com.manager.coopafi.infrastructure.valueObjectsDto;

import com.manager.coopafi.infrastructure.valueObjects.Address;
import com.manager.coopafi.personModule.enums.BrazilianState;

public record AddressDto(
        String street,
        String number,
        String neighborhood,
        String city,
        String zipCode,
        BrazilianState state
) {
    public AddressDto(Address address) {
        this(
                address.getStreet(),
                address.getAddressNumber(),
                address.getNeighborhood(),
                address.getCity(),
                address.getCep().getCepNumber(),
                address.getState()
        );
    }
}
