package com.manager.coopafi.purchaseModule.services.impl;

import com.manager.coopafi.purchaseModule.entities.InputProduct;
import com.manager.coopafi.purchaseModule.dto.inputProduct.InputProductDto;
import com.manager.coopafi.purchaseModule.dto.inputProduct.InputProductInsertDto;
import com.manager.coopafi.purchaseModule.dto.inputProduct.InputProductMinDto;
import com.manager.coopafi.purchaseModule.dto.inputProduct.InputProductUpdateDto;
import com.manager.coopafi.produtcModule.enums.MeasureUnit;
import com.manager.coopafi.infrastructure.exceptions.DomainException;
import com.manager.coopafi.infrastructure.exceptions.ResourceNotFoundException;
import com.manager.coopafi.purchaseModule.repositories.InputProductRepository;
import com.manager.coopafi.purchaseModule.services.interfaces.InputProductService;
import com.manager.coopafi.infrastructure.valueObjects.Ncm;
import com.manager.coopafi.infrastructure.valueObjects.Price;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class InputProductServiceImpl implements InputProductService {

    private final InputProductRepository inputProductRepository;

    public InputProductServiceImpl(InputProductRepository inputProductRepository) {
        this.inputProductRepository = inputProductRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InputProductMinDto> findAllByPage(Pageable pageable, String searchTerm) {
        String search = searchTerm.replaceAll("[.\\-/]", "");
        Page<InputProduct> inputPage = inputProductRepository.findBySearchTerm(search, pageable);
        return inputPage.map(InputProductMinDto::new);
    }

    @Override
    @Transactional(readOnly = true)
    public InputProductDto findById(Long id) {
        InputProduct obj = inputProductRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado."));
        return new InputProductDto(obj);
    }

    @Override
    @Transactional
    public InputProductDto insert(InputProductInsertDto dto) {
        MeasureUnit unit = MeasureUnit.validateString(dto.measureUnit());
        InputProduct product = new InputProduct(unit, new Ncm(dto.ncmValue()), dto.productName(),
                new Price(new BigDecimal(dto.priceValue())), dto.productCode());

        inputProductRepository.save(product);
        return new InputProductDto(product);
    }

    @Override
    @Transactional
    public InputProductDto update(Long id, InputProductUpdateDto dto) {
        InputProduct product = inputProductRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado."));

        updateData(product, dto);

        product = inputProductRepository.save(product);
        return new InputProductDto(product);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        InputProduct product = inputProductRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado."));
        product.deactivate();
        inputProductRepository.save(product);
    }

    @Override
    @Transactional
    public void activate(Long id) {
        InputProduct product = inputProductRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado."));
        product.activate();
        inputProductRepository.save(product);
    }

    private void updateData(InputProduct product, InputProductUpdateDto dto) {
        if (dto.productName() != null) {
            product.updateProductName(dto.productName());
        }

        if (dto.priceValue() != null) {
            product.updatePrice(new Price(new BigDecimal(dto.priceValue())));
        }
    }
}
