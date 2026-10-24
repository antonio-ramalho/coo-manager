package com.manager.coopafi.purchaseModule.services.batchValidators;

import com.manager.coopafi.purchaseModule.dto.inputBatch.InputBatchInsertDto;

public interface BatchValidationStrategy {
    void validate(InputBatchInsertDto dto);
}
