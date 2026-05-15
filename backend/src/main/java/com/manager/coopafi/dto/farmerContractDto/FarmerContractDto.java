package com.manager.coopafi.dto.farmerContractDto;

import com.manager.coopafi.domain.entities.FarmerContract;
import com.manager.coopafi.dto.farmerItemQuota.FarmerItemQuotaDto;
import java.util.Set;
import java.util.stream.Collectors;

public record FarmerContractDto(
        Long farmerId,
        String farmerName,
        Double specificCota,
        Double accumulatedValue,

        Set<FarmerItemQuotaDto> quotas
) {
    public FarmerContractDto(FarmerContract entity) {
        this(
                entity.getFarmer().getId(),
                entity.getFarmer().getPerson().getName(),
                entity.getSpecificCota() != null ? entity.getSpecificCota().getValue().doubleValue() : null,
                entity.getAccumulatedValue().getValue().doubleValue(),
                entity.getFarmerItemQuotas().stream()
                        .map(FarmerItemQuotaDto::new)
                        .collect(Collectors.toSet())
        );
    }
}
