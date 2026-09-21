package com.manager.coopafi.infrastructure.valueObjectsDto;

import com.manager.coopafi.infrastructure.valueObjects.Address;

public record AddressDto(
        String street,
        String number,
        String neighborhood,
        String city,
        String zipCode
) {
    public AddressDto(Address address) {
        this(
                address.getStreet(),
                address.getAddressNumber(),
                address.getNeighborhood(),
                address.getCity(),
                address.getCep().getCepNumber()
        );
    }
}
