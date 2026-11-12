package com.manager.coomanager.purchaseModule.repositories.projections;

public interface TopSellingProductProjection {
    Long getProductId();

    String getProductName();

    Double getTotalQuantitySold();

    String getMeasureUnit();
}
