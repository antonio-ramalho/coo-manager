package com.manager.coopafi.dto.contract;

import com.manager.coopafi.dto.contractedProduct.ContractedProductInsertDto;
import com.manager.coopafi.dto.contractConsumer.ContractConsumerInsertDto;
import com.manager.coopafi.dto.farmerContractDto.FarmerContractInsertDto;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

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