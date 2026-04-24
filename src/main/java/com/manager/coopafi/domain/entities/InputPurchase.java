package com.manager.coopafi.domain.entities;

import com.manager.coopafi.domain.valueObjects.Price;
import com.manager.coopafi.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tb_input_purchase")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class InputPurchase implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    private LocalDateTime purchaseDate;

    @Embedded
    private Price totalValue;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @ManyToOne
    @JoinColumn(name = "farmer_id")
    private Farmer farmer;

    @OneToMany(mappedBy = "inputPurchase", cascade = CascadeType.PERSIST)
    private List<InputPurchaseItem> purchaseItems = new ArrayList<>();

    public InputPurchase(Farmer farmer) {
        this.farmer = farmer;
        this.purchaseDate = LocalDateTime.now();
        this.status = PaymentStatus.PAID;
        this.totalValue = new Price(new BigDecimal(0));
    }

    public void insertFarmer(Farmer farmer) {
        this.farmer = farmer;
    }

    public void addPurchaseItems(InputPurchaseItem item) {
        this.purchaseItems.add(item);
        this.totalValue = item.getTotalPrice().add(this.totalValue);
        item.linkToPurchase(this);
    }

    public void removePurchaseItems(InputPurchaseItem item) {
        this.purchaseItems.remove(item);
        this.totalValue = this.totalValue.subtract(item.getTotalPrice());
        item.getInputBatch().increaseQuantity(item.getQuantity());
        item.linkToPurchase(null);
    }

    public void markAsPaid() {
        this.status = PaymentStatus.PAID;
    }

    public void cancelPurchase() {
        this.status = PaymentStatus.CANCELED;
    }
}
