package com.manager.coopafi.personModule.dto.client;

import com.manager.coopafi.personModule.entities.Person;

public record ClientMinDto(Long id, String legalName, String documentNumber) {

    public ClientMinDto(Person person) {
        this(
                person.getId(),
                person.getLegalName(),
                person.getDocumentNumber()
        );
    }
}
