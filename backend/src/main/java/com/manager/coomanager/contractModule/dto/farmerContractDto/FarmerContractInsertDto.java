package com.manager.coomanager.contractModule.dto.farmerContractDto;

import com.manager.coomanager.contractModule.dto.farmerItemQuota.FarmerItemQuotaInsertDto;
import java.util.List;

public record FarmerContractInsertDto(
        Long farmerId,
        Double specificCota,

        List<FarmerItemQuotaInsertDto> quotas
) {
}
