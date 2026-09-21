package com.manager.coopafi.contractModule.dto.contract;

import com.manager.coopafi.contractModule.dto.contractedProduct.ContractedProductInsertDto;
import com.manager.coopafi.contractModule.dto.contractConsumer.ContractConsumerInsertDto;
import com.manager.coopafi.contractModule.dto.farmerContractDto.FarmerContractInsertDto;
import java.time.LocalDate;
import java.util.List;

public record ContractInsertDto (
        Long institutionId,
        LocalDate initialContractDate,
        LocalDate finalContractDate,
        Double totalContractValue,
        Double globalLimit,
        String participationRule,
        String productDeliveryRule,

        List<FarmerContractInsertDto> farmerContracts,
        List<ContractConsumerInsertDto> contractConsumers,
        List<ContractedProductInsertDto> products
) {}