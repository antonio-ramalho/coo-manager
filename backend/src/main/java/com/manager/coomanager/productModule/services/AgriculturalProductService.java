package com.manager.coomanager.productModule.services;

import com.manager.coomanager.productModule.entities.AgriculturalProduct;
import com.manager.coomanager.shared.domain.valueobjects.Ncm;
import com.manager.coomanager.shared.domain.valueobjects.Price;
import com.manager.coomanager.productModule.dto.AgriculturalProductDto;
import com.manager.coomanager.productModule.dto.AgriculturalProductInsertDto;
import com.manager.coomanager.productModule.dto.AgriculturalProductMinDto;
import com.manager.coomanager.productModule.dto.AgriculturalProductUpdateDto;
import com.manager.coomanager.productModule.enums.CultivationType;
import com.manager.coomanager.productModule.enums.MeasureUnit;
import com.manager.coomanager.productModule.enums.ProductCatalogStatus;
import com.manager.coomanager.productModule.enums.ProductGroup;
import com.manager.coomanager.shared.domain.exceptions.DomainException;
import com.manager.coomanager.productModule.repositories.AgriculturalProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class AgriculturalProductService {

    @Autowired
    private AgriculturalProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<AgriculturalProductMinDto> findAllByStatus() {
        List<AgriculturalProduct> list = productRepository.findByProductCatalogStatus(ProductCatalogStatus.ACTIVE);
        return list.stream().map(AgriculturalProductMinDto::new).toList();
    }

    @Transactional(readOnly = true)
    public AgriculturalProductDto findByStatusAndId(Long id) {
        AgriculturalProduct obj = productRepository.findByProductCatalogStatusAndId(ProductCatalogStatus.ACTIVE, id)
                .orElseThrow(() -> new DomainException("Produto agricola não encontrado."));
        return new AgriculturalProductDto(obj);
    }

    @Transactional
    public AgriculturalProductDto insert(AgriculturalProductInsertDto dto) {
        MeasureUnit unit =  MeasureUnit.validateString(dto.measureUnit());
        CultivationType type = CultivationType.validateString(dto.cultivationType());
        ProductGroup group = ProductGroup.validateString(dto.productGroup());
        AgriculturalProduct product  = new AgriculturalProduct(unit, new Ncm(dto.ncmValue()), dto.productName(),
                new Price(new BigDecimal(dto.priceValue())), type, group);

        productRepository.save(product);
        return new AgriculturalProductDto(product);
    }

    @Transactional
    public AgriculturalProductDto update(Long id, AgriculturalProductUpdateDto dto) {
        AgriculturalProduct product = productRepository.findByProductCatalogStatusAndId(ProductCatalogStatus.ACTIVE, id)
                .orElseThrow(() -> new DomainException("Produto não encontrado."));

        updateData(product, dto);

        return new AgriculturalProductDto(product);
    }

    @Transactional
    public void delete(Long id) {
        AgriculturalProduct product = productRepository.findByProductCatalogStatusAndId(ProductCatalogStatus.ACTIVE, id)
                .orElseThrow(() -> new DomainException("Produto não encontrado."));

        product.deactivate();
        productRepository.save(product);
    }

    private void updateData(AgriculturalProduct product, AgriculturalProductUpdateDto dto) {

        if (dto.productName() != null) {
            product.updateProductName(dto.productName());
        }

        if (dto.cultivationType() != null) {
            CultivationType type =  CultivationType.validateString(dto.cultivationType());
            product.changeCultivationType(type);
        }

        if (dto.productGroup() != null) {
            ProductGroup group = ProductGroup.validateString(dto.productGroup());
            product.changeProductGroup(group);
        }

        if (dto.priceValue() != null) {
            product.updatePrice(new Price(new BigDecimal(dto.priceValue())));
        }
    }
}
