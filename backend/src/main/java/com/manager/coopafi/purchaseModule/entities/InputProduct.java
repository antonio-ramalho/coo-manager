package com.manager.coopafi.purchaseModule.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.manager.coopafi.infrastructure.valueObjects.Ncm;
import com.manager.coopafi.infrastructure.valueObjects.Price;
import com.manager.coopafi.produtcModule.entities.Product;
import com.manager.coopafi.produtcModule.enums.MeasureUnit;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_input_product")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InputProduct extends Product {

    private String productCode;

    @JsonIgnore
    @OneToMany(mappedBy = "inputProduct", cascade = CascadeType.PERSIST, orphanRemoval = false)
    private List<InputBatch> inputBatches = new ArrayList<>();

    public InputProduct(MeasureUnit measureUnit, Ncm ncm, String productName, Price productPrice,
                        String productCode) {
        super(measureUnit, ncm, productName, productPrice);
        this.productCode = productCode;
    }

    public void addInputBatch(InputBatch batch) {
        this.inputBatches.add(batch);
        batch.setInputProduct(this);
    }

    public void removeInputBatch(InputBatch batch) {
        this.inputBatches.remove(batch);
        batch.setInputProduct(null);
    }
}
