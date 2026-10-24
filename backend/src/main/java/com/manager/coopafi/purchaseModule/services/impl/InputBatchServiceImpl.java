package com.manager.coopafi.purchaseModule.services.impl;

import com.manager.coopafi.infrastructure.exceptions.ResourceNotFoundException;
import com.manager.coopafi.infrastructure.valueObjects.ExpirationDate;
import com.manager.coopafi.infrastructure.valueObjects.Quantity;
import com.manager.coopafi.produtcModule.entities.InputProduct;
import com.manager.coopafi.produtcModule.repositories.InputProductRepository;
import com.manager.coopafi.purchaseModule.dto.inputBatch.InputBatchDto;
import com.manager.coopafi.purchaseModule.dto.inputBatch.InputBatchInsertDto;
import com.manager.coopafi.purchaseModule.dto.inputBatch.InputBatchMinDto;
import com.manager.coopafi.purchaseModule.dto.inputBatch.InputBatchTableDto;
import com.manager.coopafi.purchaseModule.dto.inputBatch.InputBatchUpdateDto;
import com.manager.coopafi.purchaseModule.entities.InputBatch;
import com.manager.coopafi.purchaseModule.enums.ProductInventoryStatus;
import com.manager.coopafi.purchaseModule.repositories.InputBatchRepository;
import com.manager.coopafi.purchaseModule.services.batchValidators.BatchValidationStrategy;
import com.manager.coopafi.purchaseModule.services.interfaces.InputBatchService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class InputBatchServiceImpl implements InputBatchService {

    private final InputBatchRepository batchRepository;

    private final InputProductRepository productRepository;

    private final List<BatchValidationStrategy> validationStrategies;

    public InputBatchServiceImpl(InputBatchRepository batchRepository, InputProductRepository productRepository,
            List<BatchValidationStrategy> validationStrategies) {
        this.batchRepository = batchRepository;
        this.productRepository = productRepository;
        this.validationStrategies = validationStrategies;
    }

    @Override
    @Transactional(readOnly = true)
    public List<InputBatchMinDto> findAllByStatus() {
        List<InputBatch> list = batchRepository.findByProductStatus(ProductInventoryStatus.IN_STOCK);
        return list.stream().map(InputBatchMinDto::new).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InputBatchTableDto> searchBatches(int page, int size, String searchTerm) {
        Pageable pageable = PageRequest.of(page, size);
        Page<InputBatch> batchPage = batchRepository.searchBatches(searchTerm, pageable);
        return batchPage.map(InputBatchTableDto::new);
    }

    @Override
    @Transactional(readOnly = true)
    public InputBatchDto findById(Long id) {
        InputBatch batch = batchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lote não encontrado"));
        return new InputBatchDto(batch);
    }

    @Override
    @Transactional
    public InputBatchDto insert(InputBatchInsertDto dto) {

        if (validationStrategies != null) {
            validationStrategies.forEach(strategy -> strategy.validate(dto));
        }

        InputProduct product = productRepository
                .findById(dto.inputProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Produto não cadastrado ou inativo"));

        Quantity quantity = new Quantity(new BigDecimal(dto.quantityValue()));
        InputBatch batch = new InputBatch(quantity, product, quantity, new ExpirationDate(dto.expirationDate()));

        batchRepository.save(batch);
        return new InputBatchDto(batch);
    }

    @Override
    @Transactional
    public InputBatchDto update(Long id, InputBatchUpdateDto dto) {
        InputBatch batch = batchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lote não encontrado"));

        if (dto.expirationDate() != null) {
            batch.updateExpirationDate(new ExpirationDate(dto.expirationDate()));
        }
        return new InputBatchDto(batchRepository.save(batch));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        InputBatch batch = batchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lote não encontrado"));

        batch.deactivateByUser();
        batchRepository.save(batch);
    }
}
