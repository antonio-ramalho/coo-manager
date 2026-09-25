package com.manager.coopafi.personModule.dto.client;

import com.manager.coopafi.personModule.entities.Person;
import com.manager.coopafi.personModule.enums.Status;

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
