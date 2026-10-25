package com.manager.coopafi.purchaseModule.dto.inputProduct;

import com.manager.coopafi.purchaseModule.entities.InputProduct;
import com.manager.coopafi.infrastructure.valueObjects.Price;
import com.manager.coopafi.purchaseModule.enums.ProductInventoryStatus;
import java.util.List;

public record InputProductDto(
        Long id,
        String productName,
        String productCode,
        String measureUnit,
        String ncm,
        Price price,
        String productCatalogStatus,
        List<InputProductBatchSummaryDto> batches) {

    public InputProductDto(InputProduct entity) {
        this(
                entity.getId(),
                entity.getProductName(),
                entity.getProductCode(),
                entity.getMeasureUnit().name(),
                entity.getNcm().getCode(),
                entity.getProductPrice(),
                entity.getProductCatalogStatus().name(),
                entity.getInputBatches() != null
                        ? entity.getInputBatches().stream()
                                .filter(batch -> batch.getProductStatus() != ProductInventoryStatus.INACTIVE)
                                .map(InputProductBatchSummaryDto::new)
                                .toList()
                        : List.of());
    }

    public InputProductDto(Long id, String productName, String productCode, String measureUnit, String ncm, Price price,
            String productCatalogStatus) {
        this(id, productName, productCode, measureUnit, ncm, price, productCatalogStatus, List.of());
    }
}
