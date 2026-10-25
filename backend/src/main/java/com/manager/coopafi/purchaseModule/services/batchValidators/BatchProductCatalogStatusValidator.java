package com.manager.coopafi.purchaseModule.services.batchValidators;

import com.manager.coopafi.infrastructure.exceptions.DomainException;
import com.manager.coopafi.infrastructure.exceptions.ResourceNotFoundException;
import com.manager.coopafi.purchaseModule.entities.InputProduct;
import com.manager.coopafi.purchaseModule.repositories.InputProductRepository;
import com.manager.coopafi.purchaseModule.dto.inputBatch.InputBatchInsertDto;
import org.springframework.stereotype.Component;

/**
 * Validador de negócio: Impede o cadastro de lote para produtos que não estejam
 * com status ATIVO no catálogo de produtos.
 */
@Component
public class BatchProductCatalogStatusValidator implements BatchValidationStrategy {

    private final InputProductRepository productRepository;

    public BatchProductCatalogStatusValidator(InputProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void validate(InputBatchInsertDto dto) {
        if (dto.inputProductId() == null) {
            throw new DomainException("O identificador do produto é obrigatório.");
        }

        InputProduct product = productRepository.findById(dto.inputProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Produto não cadastrado ou inativo"));

        if (product.getProductCatalogStatus() == null || !product.getProductCatalogStatus().canReceiveBatches()) {
            throw new DomainException("Não é permitido cadastrar lote para um produto que não esteja com status ATIVO no catálogo.");
        }
    }
}
