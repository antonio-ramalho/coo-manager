package com.manager.coopafi.purchaseModule.entities;

import com.manager.coopafi.baseModule.BaseReceipt;
import com.manager.coopafi.personModule.entities.Person;
import com.manager.coopafi.purchaseModule.enums.PaymentStatus;
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
public class InputPurchase extends BaseReceipt<InputPurchaseItem> {

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    @OneToMany(mappedBy = "inputPurchase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InputPurchaseItem> purchaseItems = new ArrayList<>();

    public InputPurchase(Person person) {
        super();
        this.person = person;
    }

    @Override
    public void addItem(InputPurchaseItem item) {
        checkIsPending();
        this.purchaseItems.add(item);
        this.totalValue = this.totalValue.add(item.getTotalPrice());
        item.getInputBatch().decreaseQuantity(item.getQuantity());
        item.linkToBaseReceipt(this);
    }

    private void checkIsPending() {
        this.status.validateCanModify();
    }

    public void cancelPurchase() {
        this.status.validateCanTransitionTo(PaymentStatus.CANCELED);
        this.status = PaymentStatus.CANCELED;

        for (InputPurchaseItem item : this.purchaseItems) {
            item.getInputBatch().increaseQuantity(item.getQuantity());
        }
    }
}
