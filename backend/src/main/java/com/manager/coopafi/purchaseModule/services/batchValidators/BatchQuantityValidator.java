package com.manager.coopafi.purchaseModule.services.batchValidators;

import com.manager.coopafi.infrastructure.exceptions.DomainException;
import com.manager.coopafi.purchaseModule.dto.inputBatch.InputBatchInsertDto;
import org.springframework.stereotype.Component;

@Component
public class BatchQuantityValidator implements BatchValidationStrategy {

    @Override
    public void validate(InputBatchInsertDto dto) {
        if (dto.quantityValue() == null || dto.quantityValue() <= 0) {
            throw new DomainException("A quantidade inicial do lote deve ser superior a zero.");
        }
    }
}
