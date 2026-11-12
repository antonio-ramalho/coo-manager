package com.manager.coomanager.produtcModule.entities;

import com.manager.coomanager.infrastructure.valueObjects.Ncm;
import com.manager.coomanager.infrastructure.valueObjects.Price;
import com.manager.coomanager.produtcModule.enums.CultivationType;
import com.manager.coomanager.produtcModule.enums.MeasureUnit;
import com.manager.coomanager.produtcModule.enums.ProductGroup;
import com.manager.coomanager.infrastructure.exceptions.DomainException;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_agricultural_product")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AgriculturalProduct extends Product {

    @Enumerated(EnumType.STRING)
    private CultivationType cultivationType;

    @Enumerated(EnumType.STRING)
    private ProductGroup productGroup;

    public AgriculturalProduct(MeasureUnit measureUnit, Ncm ncm, String productName, Price productPrice,
                               CultivationType cultivationType, ProductGroup productGroup) {
        super(measureUnit, ncm, productName, productPrice);
        this.cultivationType = cultivationType;
        this.productGroup = productGroup;
    }

    public void changeCultivationType(CultivationType newCultivationType) {
        if (newCultivationType == null) {
            throw new DomainException("O tipo do produto não pode ser vazio.");
        }
        this.cultivationType = newCultivationType;
    }

    public void changeProductGroup(ProductGroup newProductGroup) {
        if (newProductGroup == null) {
            throw new DomainException("O grupo do produto não pode ser vazio.");
        }
        this.productGroup = newProductGroup;
    }
}
