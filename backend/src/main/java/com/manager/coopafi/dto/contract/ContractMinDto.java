package com.manager.coopafi.dto.contract;

import com.manager.coopafi.domain.entities.Contract;

public record ContractMinDto (
    Long id,
    String institutionName,
    Double contractBalance,
    Double totalContractValue,
    String documentStatus
){
    public ContractMinDto(Contract entity) {
        this(
                entity.getId(),
                entity.getInstitution().getJuridicPerson().getTradeName(),
                entity.getContractBalance().getValue().doubleValue(),
                entity.getTotalContractValue().getValue().doubleValue(),
                entity.getDocumentStatus().getDescription()
        );
    }
}
