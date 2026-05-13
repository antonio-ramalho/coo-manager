package com.manager.coopafi.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.manager.coopafi.domain.valueObjects.ExpirationDate;
import com.manager.coopafi.domain.valueObjects.Ncm;
import com.manager.coopafi.domain.valueObjects.Price;
import com.manager.coopafi.enums.MeasureUnit;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tb_input_product")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InputProduct extends Product{

    private String productCode;

    @Embedded
    private ExpirationDate expirationDate;
    private LocalDate entryDate;

    @JsonIgnore
    @OneToMany(mappedBy = "inputProduct", cascade = CascadeType.PERSIST, orphanRemoval = false)
    private List<InputBatch> inputBatches = new ArrayList<>();

    public InputProduct(MeasureUnit measureUnit, Ncm ncm, String productName, Price productPrice,
                        ExpirationDate expirationDate, String productCode) {
        super(measureUnit, ncm, productName, productPrice);
        this.expirationDate = expirationDate;
        this.productCode = productCode;
        this.entryDate = LocalDate.now();
    }

    public void updateExpirationDate(ExpirationDate newDate) {
        this.expirationDate = Objects.requireNonNull(newDate);
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
