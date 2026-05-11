package com.manager.coopafi.services;

import com.manager.coopafi.domain.entities.*;
import com.manager.coopafi.domain.valueObjects.*;
import com.manager.coopafi.dto.inputProduct.InputProductDto;
import com.manager.coopafi.dto.inputProduct.InputProductInsertDto;
import com.manager.coopafi.dto.inputProduct.InputProductMinDto;
import com.manager.coopafi.dto.inputProduct.InputProductUpdateDto;
import com.manager.coopafi.enums.ProductCatalogStatus;
import com.manager.coopafi.exceptions.DomainException;
import com.manager.coopafi.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class InputProductService {

    @Autowired
    private InputProductRepository inputProductRepository;

    @Transactional(readOnly = true)
    public List<InputProductMinDto> findByStatus() {
        List<InputProduct> list = inputProductRepository.findByProductCatalogStatus(ProductCatalogStatus.ACTIVE);
        return list.stream().map(InputProductMinDto::new).toList();
    }

    @Transactional(readOnly = true)
    public InputProductDto findByStatusAndId(Long id) {
        Optional<InputProduct> obj = inputProductRepository.findByProductCatalogStatusAndId(ProductCatalogStatus.ACTIVE, id);
        if (obj.isEmpty()) {
            throw new DomainException("Produto não encontrado.");
        }
        return new InputProductDto(obj.get());
    }

    @Transactional
    public InputProductDto insert(InputProductInsertDto dto) {
        return null;
    }

    @Transactional
    public InputProductDto update(Long id, InputProductUpdateDto dto) {
        InputProduct product  = inputProductRepository.findByProductCatalogStatusAndId(ProductCatalogStatus.ACTIVE, id)
                .orElseThrow(() -> new DomainException("Produto não encontrado."));

        product = inputProductRepository.save(product);
        return new InputProductDto(product);
    }

    // Verificar método de deactivate de InputProduct
    /*
    @Transactional
    public void delete(Long id) {
        InputProduct product = inputProductRepository.findByProductCatalogStatusAndId(ProductCatalogStatus.ACTIVE, id)
                .orElseThrow(() -> new DomainException("Produto não encontrado."));
        product.deactivate();
        inputProductRepository.save(product);
    }
    */
}
