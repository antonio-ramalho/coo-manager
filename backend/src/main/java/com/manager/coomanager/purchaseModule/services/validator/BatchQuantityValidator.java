package com.manager.coomanager.purchaseModule.services.validator;

import com.manager.coomanager.infrastructure.exceptions.DomainException;
import com.manager.coomanager.purchaseModule.dto.inputBatch.InputBatchInsertDto;
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
