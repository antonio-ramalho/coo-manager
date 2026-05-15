package com.manager.coopafi.dto.contract;

import com.manager.coopafi.dto.contractedProduct.ContractedProductInsertDto;
import com.manager.coopafi.dto.contractConsumer.ContractConsumerInsertDto;
import com.manager.coopafi.dto.farmerContractDto.FarmerContractInsertDto;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public record ContractUpdateDto(
        LocalDate initialContractDate,
        LocalDate finalContractDate,
        Double globalLimit,
        String participationRule,
        String productDeliveryRule,

        Set<FarmerContractInsertDto> farmerContracts,
        Set<ContractConsumerInsertDto> contractConsumers,
        List<ContractedProductInsertDto> products
) {
}
