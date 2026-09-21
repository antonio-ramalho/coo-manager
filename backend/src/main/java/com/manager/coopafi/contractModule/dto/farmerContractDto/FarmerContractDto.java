package com.manager.coopafi.contractModule.dto.farmerContractDto;

import com.manager.coopafi.contractModule.entities.FarmerContract;
import com.manager.coopafi.contractModule.dto.farmerItemQuota.FarmerItemQuotaDto;
import java.util.List;
import java.util.stream.Collectors;

public record FarmerContractDto(
        Long farmerId,
        String clientName,
        Double specificCota,
        Double accumulatedValue,

        List<FarmerItemQuotaDto> quotas
) {
    public FarmerContractDto(FarmerContract entity) {
        this(
                entity.getFarmer().getId(),
                entity.getFarmer().getPerson().getLegalName(),
                entity.getSpecificCota() != null ? entity.getSpecificCota().getValue().doubleValue() : null,
                entity.getAccumulatedValue().getValue().doubleValue(),
                entity.getFarmerItemQuotas().stream()
                        .map(FarmerItemQuotaDto::new)
                        .collect(Collectors.toList())
        );
    }
}
