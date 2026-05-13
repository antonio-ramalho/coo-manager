package com.manager.coopafi.services;

import com.manager.coopafi.domain.entities.InputBatch;
import com.manager.coopafi.domain.entities.InputProduct;
import com.manager.coopafi.domain.valueObjects.ExpirationDate;
import com.manager.coopafi.domain.valueObjects.Quantity;
import com.manager.coopafi.dto.inputBatch.InputBatchDto;
import com.manager.coopafi.dto.inputBatch.InputBatchInsertDto;
import com.manager.coopafi.dto.inputBatch.InputBatchMinDto;
import com.manager.coopafi.dto.inputBatch.InputBatchUpdateDto;
import com.manager.coopafi.enums.ProductCatalogStatus;
import com.manager.coopafi.enums.ProductInventoryStatus;
import com.manager.coopafi.exceptions.DomainException;
import com.manager.coopafi.repositories.InputBatchRepository;
import com.manager.coopafi.repositories.InputProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class InputBatchService {

    @Autowired
    private InputBatchRepository batchRepository;

    @Autowired
    private InputProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<InputBatchMinDto> findAllByStatus() {
       List<InputBatch> list = batchRepository.findByProductStatus(ProductInventoryStatus.IN_STOCK);
       return list.stream().map(InputBatchMinDto::new).toList();
    }

    @Transactional(readOnly = true)
    public InputBatchDto findById(Long id) {
       InputBatch batch = batchRepository.findById(id).orElseThrow(() -> new DomainException("Lote não encontrado"));
       return new InputBatchDto(batch);
    }

    @Transactional
    public InputBatchDto insert(InputBatchInsertDto dto) {
        InputProduct product = productRepository.findByProductCatalogStatusAndId(ProductCatalogStatus.ACTIVE,
                        dto.inputProductId()).orElseThrow(() -> new DomainException("Produto não cadastrado."));

        Quantity quantity = new Quantity(new BigDecimal(dto.quantityValue()));
        InputBatch batch = new InputBatch(quantity, product, quantity, new ExpirationDate(dto.expirationDate()));

        batchRepository.save(batch);
        return new InputBatchDto(batch);
    }

    @Transactional
    public InputBatchDto update(Long id, InputBatchUpdateDto dto) {
        InputBatch batch = batchRepository.findById(id).orElseThrow(() -> new DomainException("Lote não cadastrado."));

        if (dto.expirationDate() != null) {
            batch.updateExpirationDate(new ExpirationDate(dto.expirationDate()));
        }
        return new InputBatchDto(batchRepository.save(batch));
    }

    @Transactional
    public void delete(Long id) {
        InputBatch batch = batchRepository.findById(id)
                .orElseThrow(() -> new DomainException("Lote não encontrado."));

        batch.deactivateByUser();
        batchRepository.save(batch);
    }
}