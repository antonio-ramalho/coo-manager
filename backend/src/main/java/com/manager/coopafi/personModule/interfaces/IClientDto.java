package com.manager.coopafi.personModule.interfaces;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.manager.coopafi.infrastructure.valueObjectsDto.AddressDto;
import com.manager.coopafi.personModule.dto.client.JuridicClientDto;
import com.manager.coopafi.personModule.dto.client.NaturalClientDto;
import com.manager.coopafi.personModule.enums.Status;

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
