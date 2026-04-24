package com.manager.coopafi.domain.entities;

import com.manager.coopafi.domain.valueObjects.Ncm;
import com.manager.coopafi.domain.valueObjects.Price;
import com.manager.coopafi.enums.CultivationType;
import com.manager.coopafi.enums.MeasureUnit;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_agricultural_product")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AgriculturalProduct extends Product {

    @Enumerated(EnumType.STRING)
    private CultivationType cultivationType;

    public AgriculturalProduct(MeasureUnit measureUnit, Ncm ncm, String productName, Price productPrice,
                               CultivationType cultivationType) {
        super(measureUnit, ncm, productName, productPrice);
        this.cultivationType = cultivationType;
    }
}
