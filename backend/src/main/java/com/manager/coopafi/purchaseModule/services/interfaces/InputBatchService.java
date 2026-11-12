package com.manager.coopafi.purchaseModule.services.interfaces;

import com.manager.coopafi.purchaseModule.dto.inputBatch.InputBatchDto;
import com.manager.coopafi.purchaseModule.dto.inputBatch.InputBatchInsertDto;
import com.manager.coopafi.purchaseModule.dto.inputBatch.InputBatchMinDto;
import com.manager.coopafi.purchaseModule.dto.inputBatch.InputBatchTableDto;
import com.manager.coopafi.purchaseModule.dto.inputBatch.InputBatchUpdateDto;
import org.springframework.data.domain.Page;
import java.util.List;

public interface InputBatchService {

    List<InputBatchMinDto> findAllByStatus();

    default Page<InputBatchTableDto> searchBatches(int page, int size, String searchTerm) {
        return searchBatches(page, size, searchTerm, false);
    }

    Page<InputBatchTableDto> searchBatches(int page, int size, String searchTerm, boolean onlyInStock);

    InputBatchDto findById(Long id);

    InputBatchDto insert(InputBatchInsertDto dto);

    InputBatchDto update(Long id, InputBatchUpdateDto dto);

    void delete(Long id);

    void activate(Long id);
}