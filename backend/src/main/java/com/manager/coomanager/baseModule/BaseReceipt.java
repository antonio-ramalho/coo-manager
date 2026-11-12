package com.manager.coomanager.baseModule;

import com.manager.coomanager.infrastructure.valueObjects.Price;
import com.manager.coomanager.purchaseModule.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class BaseReceipt<T extends BaseReceiptItem<?>> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(nullable = false, updatable = false, unique = true, length = 36)
    private UUID uuid;

    protected LocalDateTime emissionDate;

    @Embedded
    protected Price totalValue;

    @Enumerated(EnumType.STRING)
    protected PaymentStatus status;

    public BaseReceipt() {
        this.uuid = UUID.randomUUID();
        this.emissionDate = LocalDateTime.now();
        this.status = PaymentStatus.PENDING;
        this.totalValue = new Price(BigDecimal.ZERO);
    }

    @PrePersist
    protected void ensureUuid() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID();
        }
    }

    public abstract void addItem(T item);

    public void markAsPaid() {
        this.status.validateCanTransitionTo(PaymentStatus.PAID);
        this.status = PaymentStatus.PAID;
    }
}
