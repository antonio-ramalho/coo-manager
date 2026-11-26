package com.manager.coomanager.customerModule.dto.client;

import com.manager.coomanager.shared.domain.person.Person;
import com.manager.coomanager.shared.domain.enums.Status;

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
