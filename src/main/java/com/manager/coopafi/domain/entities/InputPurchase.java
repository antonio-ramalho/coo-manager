package com.manager.coopafi.domain.entities;

import com.manager.coopafi.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_input_purchase")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InputPurchase extends BaseReceipt <InputPurchaseItem> {

    @ManyToOne
    @JoinColumn(name = "farmer_id")
    private Farmer farmer;

    @OneToMany(mappedBy = "inputPurchase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InputPurchaseItem> purchaseItems = new ArrayList<>();

    public InputPurchase(Farmer farmer) {
        super();
        this.farmer = farmer;
    }

    public void insertFarmer(Farmer farmer) {
        this.farmer = farmer;
    }

    @Override
    public void addItem(InputPurchaseItem item) {
        this.purchaseItems.add(item);
        this.totalValue = item.getTotalPrice().add(this.totalValue);
        item.linkToBaseReceipt(this);
    }

    @Override
    public void removeItem(InputPurchaseItem item) {
        this.purchaseItems.remove(item);
        this.totalValue = this.totalValue.subtract(item.getTotalPrice());
        item.getInputBatch().increaseQuantity(item.getQuantity());
        item.linkToBaseReceipt(null);
    }

    public void cancelPurchase() {
        this.status = PaymentStatus.CANCELED;
    }
}
