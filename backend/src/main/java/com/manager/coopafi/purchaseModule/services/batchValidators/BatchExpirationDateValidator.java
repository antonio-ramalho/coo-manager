package com.manager.coopafi.purchaseModule.services.batchValidators;

import com.manager.coopafi.infrastructure.exceptions.DomainException;
import com.manager.coopafi.purchaseModule.dto.inputBatch.InputBatchInsertDto;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class BatchExpirationDateValidator implements BatchValidationStrategy {

    @Override
    public void validate(InputBatchInsertDto dto) {
        if (dto.expirationDate() == null) {
            throw new DomainException("Data de validade é obrigatória.");
        }
        if (dto.expirationDate().isBefore(LocalDate.now())) {
            throw new DomainException("A data de validade não pode ser anterior à data atual.");
        }
    }
}
