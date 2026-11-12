package com.manager.coomanager.purchaseModule.services.validator;

import com.manager.coomanager.infrastructure.exceptions.DomainException;
import com.manager.coomanager.purchaseModule.dto.inputBatch.InputBatchInsertDto;
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
