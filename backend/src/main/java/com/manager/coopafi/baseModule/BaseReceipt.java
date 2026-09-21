package com.manager.coopafi.baseModule;

import com.manager.coopafi.infrastructure.valueObjects.Price;
import com.manager.coopafi.purchaseModule.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class BaseReceipt <T extends BaseReceiptItem<?>> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    protected LocalDateTime emissionDate;

    @Embedded
    protected Price totalValue;

    @Enumerated(EnumType.STRING)
    protected PaymentStatus status;

    public  BaseReceipt() {
        this.emissionDate = LocalDateTime.now();
        this.status = PaymentStatus.PENDING;
        this.totalValue = new Price(BigDecimal.ZERO);
    }

    public abstract void addItem(T item);

    public void markAsPaid() {
        this.status = PaymentStatus.PAID;
    }
}
