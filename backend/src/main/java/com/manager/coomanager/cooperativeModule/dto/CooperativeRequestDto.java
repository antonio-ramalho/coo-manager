package com.manager.coomanager.cooperativeModule.dto;

import com.manager.coomanager.shared.domain.valueobjects.dto.AddressDto;
import java.time.LocalDate;

public record CooperativeRequestDto(
                String legalName,
                String tradeName,
                String cnpj,
                String stateRegistration,
                String email,
                String phone,
                LocalDate foundationDate,
                AddressDto address) {
}
