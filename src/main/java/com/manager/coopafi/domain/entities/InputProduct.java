package com.manager.coopafi.domain.entities;

import com.manager.coopafi.domain.valueObjects.Ncm;
import com.manager.coopafi.domain.valueObjects.Price;
import com.manager.coopafi.enums.MeasureUnit;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "tb_input_product")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InputProduct extends Product{

    private String productCode;
    private LocalDate expirationDate;

    public InputProduct(MeasureUnit measureUnit, Ncm ncm, String productName, Price productPrice,
                        LocalDate expirationDate, String productCode) {
        super(measureUnit, ncm, productName, productPrice);
        this.expirationDate = expirationDate;
        this.productCode = productCode;
    }
}
