package com.manager.coopafi.contractModule.dto.farmerContractDto;

import com.manager.coopafi.contractModule.dto.farmerItemQuota.FarmerItemQuotaInsertDto;
import java.util.List;

public record FarmerContractInsertDto(
        Long farmerId,
        Double specificCota,

        List<FarmerItemQuotaInsertDto> quotas
) {
}
