package com.manager.coomanager.personModule.dto.client;

import com.manager.coomanager.personModule.entities.Person;
import com.manager.coomanager.personModule.enums.Status;

public record ClientMinDto(Long id, String legalName, String documentNumber, Status status) {

    public ClientMinDto(Person person) {
        this(
                person.getId(),
                person.getLegalName(),
                person.getDocumentNumber(),
                person.getStatus()
        );
    }
}
