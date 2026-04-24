package com.manager.coopafi.domain.entities;

import com.manager.coopafi.domain.valueObjects.Ncm;
import com.manager.coopafi.domain.valueObjects.Price;
import com.manager.coopafi.enums.MeasureUnit;
import com.manager.coopafi.enums.ProductCatalogStatus;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "tb_product")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class Product implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    private String productName;
    @Embedded
    private Price productPrice;
    @Enumerated(EnumType.STRING)
    private MeasureUnit measureUnit;
    @Enumerated(EnumType.STRING)
    ProductCatalogStatus productCatalogStatus;
    @Embedded
    private Ncm ncm;

    public Product(MeasureUnit measureUnit, Ncm ncm, String productName, Price productPrice) {
        this.measureUnit = measureUnit;
        this.ncm = ncm;
        this.productCatalogStatus = ProductCatalogStatus.ACTIVE;
        this.productName = productName;
        this.productPrice = productPrice;
    }

    public void updatePrice(Price productPrice) {
        this.productPrice = Objects.requireNonNull(productPrice);
    }

    public void updateProductName(String productName) {
        this.productName = Objects.requireNonNull(productName);
    }

    public void activate() {
        this.productCatalogStatus = ProductCatalogStatus.ACTIVE;
    }

    public void deactivate() {
        this.productCatalogStatus = ProductCatalogStatus.INACTIVE;
    }
}
