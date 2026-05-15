package com.manager.coopafi.dto.contract;

import com.manager.coopafi.domain.entities.Contract;
import com.manager.coopafi.dto.contractConsumer.ContractConsumerDto;
import com.manager.coopafi.dto.contractedProduct.ContractedProductDto;
import com.manager.coopafi.dto.farmerContractDto.FarmerContractDto;
import com.manager.coopafi.dto.institution.InstitutionMinDto;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record ContractDto (
        Long id,
        InstitutionMinDto institution,
        Double contractBalance,
        Double totalContractValue,
        String documentStatus,
        LocalDate initialContractDate,
        LocalDate finalContractDate,
        Double globalLimit,
        String participationRule,
        String productDeliveryRule,

        Set<FarmerContractDto> farmerContracts,
        Set<ContractConsumerDto> contractConsumers,
        List<ContractedProductDto> products
){
    public ContractDto(Contract entity) {
        this(
                entity.getId(),
                new InstitutionMinDto(entity.getInstitution()),
                entity.getContractBalance().getValue().doubleValue(),
                entity.getTotalContractValue().getValue().doubleValue(),
                entity.getDocumentStatus().getDescription(),
                entity.getInitialContractDate(),
                entity.getFinalContractDate(),
                entity.getGlobalLimit().getValue().doubleValue(),
                entity.getParticipationRule().getDescription(),
                entity.getProductDeliveryRule().getDescription(),

                entity.getFarmerContracts().stream()
                        .map(FarmerContractDto::new)
                                .collect(Collectors.toSet()),


                entity.getContractConsumers().stream()
                        .map(ContractConsumerDto::new)
                        .collect(Collectors.toSet()),

                entity.getProducts().stream()
                        .map(ContractedProductDto::new)
                        .toList()
        );
    }
}
