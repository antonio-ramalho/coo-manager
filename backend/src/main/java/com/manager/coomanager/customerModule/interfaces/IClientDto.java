package com.manager.coomanager.customerModule.interfaces;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.manager.coomanager.shared.domain.valueobjects.dto.AddressDto;
import com.manager.coomanager.customerModule.dto.client.JuridicClientDto;
import com.manager.coomanager.customerModule.dto.client.NaturalClientDto;
import com.manager.coomanager.shared.domain.enums.Status;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "personType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = NaturalClientDto.class, name = "NATURAL"),
        @JsonSubTypes.Type(value = JuridicClientDto.class, name = "JURIDIC")
})
public interface IClientDto {
    Long id();
    String legalName();
    String email();
    String phone();
    AddressDto address();
    Status status();
}
