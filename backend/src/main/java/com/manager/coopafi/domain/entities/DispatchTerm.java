package com.manager.coopafi.domain.entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_dispatch_terms")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DispatchTerm extends BaseReceipt<DispatchTermItem> {

    @ManyToOne
    @JoinColumn(name = "consumer_unit_id")
    private ConsumerUnit consumerUnit;

    @OneToMany(mappedBy = "dispatchTerm", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DispatchTermItem> dispatchItems = new ArrayList<>();

    public DispatchTerm(ConsumerUnit consumerUnit) {
        super();
        this.consumerUnit = consumerUnit;
    }

    @Override
    public void addItem(DispatchTermItem item) {
        this.dispatchItems.add(item);
        this.totalValue = item.getTotalPrice().add(this.totalValue);
        item.linkToBaseReceipt(this);
    }

    @Override
    public void removeItem(DispatchTermItem item) {
        if (this.dispatchItems.remove(item)) {
            this.totalValue = this.totalValue.subtract(item.getTotalPrice());
            item.linkToBaseReceipt(null);
        }
    }
}
