package com.manager.coomanager.contractModule.dto.contract;

import com.manager.coomanager.contractModule.entities.Contract;

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
                entity.calculateBalance().getValue().doubleValue(),
                entity.getTotalContractValue().getValue().doubleValue(),
                entity.getDocumentStatus().getDescription()
        );
    }
}
