package com.manager.coomanager.purchaseModule.services.validator;

import com.manager.coomanager.purchaseModule.dto.inputBatch.InputBatchInsertDto;

public interface BatchValidationStrategy {
    void validate(InputBatchInsertDto dto);
}
