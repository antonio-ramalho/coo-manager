package com.manager.coomanager.productModule.entities;

import com.manager.coomanager.shared.domain.valueobjects.Ncm;
import com.manager.coomanager.shared.domain.valueobjects.Price;
import com.manager.coomanager.productModule.enums.MeasureUnit;
import com.manager.coomanager.productModule.enums.ProductCatalogStatus;
import com.manager.coomanager.shared.domain.exceptions.DomainException;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.manager.coomanager.shared.domain.entities.TenantEntity;
import org.hibernate.annotations.Filter;
import java.io.Serial;
import java.util.Objects;
import java.util.UUID;

@MappedSuperclass
@Filter(name = "tenantFilter", condition = "cooperative_id = :cooperativeId")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public abstract class Product extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @EqualsAndHashCode.Include
    @Column(updatable = false, unique = true, nullable = false)
    private UUID uuid;

    public Product(MeasureUnit measureUnit, Ncm ncm, String productName, Price productPrice) {
        this.measureUnit = measureUnit;
        this.ncm = ncm;
        this.productCatalogStatus = ProductCatalogStatus.ACTIVE;
        this.productName = productName;
        this.productPrice = productPrice;
        this.uuid = UUID.randomUUID();
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
