package com.manager.coopafi.purchaseModule.entities;

import com.manager.coopafi.baseModule.BaseReceipt;
import com.manager.coopafi.personModule.entities.Person;
import com.manager.coopafi.purchaseModule.enums.PaymentStatus;
import com.manager.coopafi.infrastructure.exceptions.DomainException;
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
