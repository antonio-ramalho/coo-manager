package com.manager.coopafi.domain.entities;

import com.manager.coopafi.enums.PaymentStatus;
import com.manager.coopafi.exceptions.DomainException;
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
        checkIsPending();
        this.purchaseItems.add(item);
        this.totalValue = item.getTotalPrice().add(this.totalValue);
        item.getInputBatch().decreaseQuantity(item.getQuantity());
        item.linkToBaseReceipt(this);
    }

    private void checkIsPending() {
        if (this.status != PaymentStatus.PENDING) {
            throw new DomainException("Não é possível alterar uma compra cancelada ou paga.");
        }
    }

    public void cancelPurchase() {
        checkIsPending();
        this.status = PaymentStatus.CANCELED;

        for (InputPurchaseItem item : this.purchaseItems) {
            item.getInputBatch().increaseQuantity(item.getQuantity());
        }
    }
}
