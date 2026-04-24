package com.manager.coopafi.domain.entities;

import com.manager.coopafi.enums.ProductInventoryStatus;
import com.manager.coopafi.exceptions.DomainException;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tb_input_batch")
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class InputBatch implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne
    @JoinColumn(name = "input_product_id")
    private InputProduct inputProduct;

    @Enumerated(EnumType.STRING)
    private ProductInventoryStatus productStatus;

    private Integer originalQuantity;
    private Integer currentQuantity;

    @OneToMany(mappedBy = "inputBatch")
    private List<InputPurchaseItem> inputPurchaseItems;

    public InputBatch(Integer currentQuantity, InputProduct inputProduct, Integer originalQuantity) {
        this.currentQuantity = currentQuantity;
        this.inputProduct = inputProduct;
        this.originalQuantity = originalQuantity;
        this.productStatus = ProductInventoryStatus.IN_STOCK;
    }

    public boolean isExpired() {
        return inputProduct.getExpirationDate().isBefore(LocalDate.now());
    }

    public void updateStatusByDate() {
        if (inputProduct.getExpirationDate().isBefore(LocalDate.now())) {
            this.productStatus = ProductInventoryStatus.EXPIRED;
        }
    }

    public void checkStock(Integer amount) {
        if (amount > currentQuantity) {
            throw  new DomainException("Existem apenas: " + currentQuantity + " em estoque!");
        }
    }

    public void decreaseQuantity(Integer amount) {
        checkStock(amount);
        this.currentQuantity -= amount;
        updateStatusByStock();
    }

    public boolean isAvailable() {
        return productStatus == ProductInventoryStatus.IN_STOCK && !isExpired();
    }

    public void updateStatusByStock() {
        if (currentQuantity == 0) {
            this.productStatus = ProductInventoryStatus.OUT_OF_STOCK;
        }
    }
}
