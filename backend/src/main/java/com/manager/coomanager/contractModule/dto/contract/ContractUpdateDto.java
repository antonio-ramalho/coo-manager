package com.manager.coomanager.contractModule.dto.contract;

import com.manager.coomanager.contractModule.dto.contractedProduct.ContractedProductInsertDto;
import com.manager.coomanager.contractModule.dto.contractConsumer.ContractConsumerInsertDto;
import com.manager.coomanager.contractModule.dto.farmerContractDto.FarmerContractInsertDto;
import java.time.LocalDate;
import java.util.List;

public record ContractUpdateDto(
        LocalDate initialContractDate,
        LocalDate finalContractDate,
        Double globalLimit,
        String participationRule,
        String productDeliveryRule,

        List<FarmerContractInsertDto> farmerContracts,
        List<ContractConsumerInsertDto> contractConsumers,
        List<ContractedProductInsertDto> products
) {
}
