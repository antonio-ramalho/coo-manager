package com.manager.coopafi.purchaseModule.entities;

import com.manager.coopafi.infrastructure.valueObjects.ExpirationDate;
import com.manager.coopafi.infrastructure.valueObjects.Quantity;
import com.manager.coopafi.produtcModule.entities.InputProduct;
import com.manager.coopafi.purchaseModule.enums.ProductInventoryStatus;
import com.manager.coopafi.infrastructure.exceptions.DomainException;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_input_batch")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class InputBatch implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Column(nullable = false, updatable = false, unique = true)
    private UUID uuid;

    @Setter
    @ManyToOne
    @JoinColumn(name = "input_product_id")
    private InputProduct inputProduct;

    @Enumerated(EnumType.STRING)
    private ProductInventoryStatus productStatus;

    @Embedded
    private ExpirationDate expirationDate;

    private LocalDate entryDate;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "original_quantity"))
    private Quantity originalQuantity;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "current_quantity"))
    private Quantity currentQuantity;

    @OneToMany(mappedBy = "inputBatch")
    private List<InputPurchaseItem> inputPurchaseItems;

    public InputBatch(Quantity currentQuantity, InputProduct inputProduct,
                      Quantity originalQuantity, ExpirationDate expirationDate) {
        this.currentQuantity = currentQuantity;
        this.inputProduct = inputProduct;
        this.originalQuantity = originalQuantity;
        this.productStatus = ProductInventoryStatus.IN_STOCK;
        this.expirationDate = expirationDate;
        this.entryDate = LocalDate.now();
        this.uuid = UUID.randomUUID();
    }

    public void updateExpirationDate(@NonNull ExpirationDate newDate) {
        this.expirationDate = newDate;
    }

    public void checkStock(Quantity amount) {
        if (amount.compareTo(this.currentQuantity) > 0) {
            throw  new DomainException("Existem apenas: " + currentQuantity + " em estoque!");
        }
    }

    public void deactivateByExpirationDate() {
        this.productStatus = ProductInventoryStatus.EXPIRED;
    }

    public void deactivateByUser() {
        this.productStatus = ProductInventoryStatus.INACTIVE;
    }

    public void updateStatusByStock() {
        if (currentQuantity.equals(new Quantity(BigDecimal.ZERO))) {
            this.productStatus = ProductInventoryStatus.OUT_OF_STOCK;
        }
    }

    public void decreaseQuantity(@NonNull Quantity amount) {
        if (this.productStatus != ProductInventoryStatus.IN_STOCK ||
                this.expirationDate.isExpired()) {
            throw new DomainException("Lote vencido ou sem estoque.");
        }
        checkStock(amount);
        this.currentQuantity = this.currentQuantity.subtract(amount);
        updateStatusByStock();
    }

    public void increaseQuantity(@NonNull Quantity amount) {
        checkStock(amount);
        this.currentQuantity = this.currentQuantity.add(amount);
        updateStatusByStock();
    }
}