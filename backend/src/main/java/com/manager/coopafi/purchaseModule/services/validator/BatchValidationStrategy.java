package com.manager.coopafi.purchaseModule.services.validator;

import com.manager.coopafi.purchaseModule.dto.inputBatch.InputBatchInsertDto;

public interface BatchValidationStrategy {
    void validate(InputBatchInsertDto dto);
}
