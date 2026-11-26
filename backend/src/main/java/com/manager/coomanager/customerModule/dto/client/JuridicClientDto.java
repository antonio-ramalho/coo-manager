package com.manager.coomanager.customerModule.dto.client;

import com.manager.coomanager.shared.domain.valueobjects.dto.AddressDto;
import com.manager.coomanager.shared.domain.person.JuridicPerson;
import com.manager.coomanager.shared.domain.enums.Status;
import com.manager.coomanager.customerModule.interfaces.IClientDto;
import java.time.LocalDate;

public record JuridicClientDto(
        Long id,
        String legalName,
        AddressDto address,
        String email,
        String phone,
        Status status,
        String cnpj,
        LocalDate foundationDate,
        String tradeName
) implements IClientDto {
    public JuridicClientDto(JuridicPerson entity) {
        this(
                entity.getId(),
                entity.getLegalName(),
                new AddressDto(entity.getAddress()),
                entity.getEmail().getAddressEmail(),
                entity.getPhone().getPhoneNumber(),
                entity.getStatus(),
                entity.getCnpj().getCnpjNumber(),
                entity.getFoundationDate(),
                entity.getTradeName()
        );
    }
}
