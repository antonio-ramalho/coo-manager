package com.manager.coopafi.domain.entities;

import com.manager.coopafi.domain.valueObjects.Quantity;
import com.manager.coopafi.enums.ProductInventoryStatus;
import com.manager.coopafi.exceptions.DomainException;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
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

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "original_quantity"))
    private Quantity originalQuantity;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "current_quantity"))
    private Quantity currentQuantity;

    @OneToMany(mappedBy = "inputBatch")
    private List<InputPurchaseItem> inputPurchaseItems;

    public InputBatch(Quantity currentQuantity, InputProduct inputProduct, Quantity originalQuantity) {
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

    public void checkStock(Quantity amount) {
        if (amount.compareTo(this.currentQuantity) > 0) {
            throw  new DomainException("Existem apenas: " + currentQuantity + " em estoque!");
        }
    }

    public void decreaseQuantity(@NonNull Quantity amount) {
        checkStock(amount);
        this.currentQuantity = this.currentQuantity.subtract(amount);
        updateStatusByStock();
    }

    public void increaseQuantity(@NonNull Quantity amount) {
        checkStock(amount);
        this.currentQuantity = this.currentQuantity.add(amount);
        updateStatusByStock();
    }

    public boolean isAvailable() {
        return productStatus == ProductInventoryStatus.IN_STOCK && !isExpired();
    }

    public void updateStatusByStock() {
        if (currentQuantity.equals(new Quantity(BigDecimal.ZERO))) {
            this.productStatus = ProductInventoryStatus.OUT_OF_STOCK;
        }
    }
}
