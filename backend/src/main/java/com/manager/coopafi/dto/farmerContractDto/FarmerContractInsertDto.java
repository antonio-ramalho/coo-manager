package com.manager.coopafi.dto.farmerContractDto;

import com.manager.coopafi.dto.farmerItemQuota.FarmerItemQuotaInsertDto;
import java.util.List;

public record FarmerContractInsertDto(
        Long farmerId,
        Double specificCota,

        List<FarmerItemQuotaInsertDto> quotas
) {
}
