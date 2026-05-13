package com.manager.coopafi.domain.entities;

import com.manager.coopafi.domain.valueObjects.Ncm;
import com.manager.coopafi.domain.valueObjects.Price;
import com.manager.coopafi.enums.MeasureUnit;
import com.manager.coopafi.enums.ProductCatalogStatus;
import com.manager.coopafi.exceptions.DomainException;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@MappedSuperclass
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
    private ProductCatalogStatus productCatalogStatus;
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
        this.productPrice = Objects.requireNonNull(productPrice, "O novo preço não pode ser nulo.");
    }

    public void updateProductName(String productName) {
        this.productName = Objects.requireNonNull(productName, "O novo nome não pode ser nulo.");
    }

    public void activate() {
        if (this.productCatalogStatus == ProductCatalogStatus.ACTIVE) {
            throw new DomainException("Este produto já está ativo.");
        }
        this.productCatalogStatus = ProductCatalogStatus.ACTIVE;
    }

    public void deactivate() {
        if (this.productCatalogStatus == ProductCatalogStatus.INACTIVE) {
            throw new DomainException("Este produto já está inativo.");
        }
        this.productCatalogStatus = ProductCatalogStatus.INACTIVE;
    }
}
