package com.manager.coomanager.infrastructure.valueObjectsDto;

import com.manager.coomanager.infrastructure.valueObjects.Address;
import com.manager.coomanager.personModule.enums.BrazilianState;

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
