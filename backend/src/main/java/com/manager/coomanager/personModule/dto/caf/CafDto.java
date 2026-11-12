package com.manager.coomanager.personModule.dto.caf;

import com.manager.coomanager.personModule.entities.Caf;
import com.manager.coomanager.personModule.entities.Farmer;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record CafDto(
        Long id,
        String cafNumber,
        String settlementType,
        LocalDate expirationDate,
        String documentStatus,
        List<Long> farmerIds
) {
    public CafDto(Caf entity) {
        this(
                entity.getId(),
                entity.getCafNumber().getValue(),
                entity.getSettlementType().name(),
                entity.getExpirationDate().getValue(),
                entity.getDocumentStatus().name(),
                entity.getFarmers().stream().map(Farmer::getId).collect(Collectors.toList())
        );
    }
}
