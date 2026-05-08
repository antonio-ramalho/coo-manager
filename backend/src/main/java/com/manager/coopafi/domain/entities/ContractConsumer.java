package com.manager.coopafi.domain.entities;

import com.manager.coopafi.enums.DocumentStatus;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "tb_contract_consumers")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ContractConsumer implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    private LocalDate entryDate;

    @Enumerated(EnumType.STRING)
    private DocumentStatus status;

    @ManyToOne
    @JoinColumn(name = "contract_id")
    private Contract contract;

    @ManyToOne
    @JoinColumn(name = "consumer_id")
    private ConsumerUnit consumer;

    public ContractConsumer(Contract contract, ConsumerUnit consumer) {
        this.contract = Objects.requireNonNull(contract);
        this.consumer = Objects.requireNonNull(consumer);
        this.entryDate = LocalDate.now();
        this.status = DocumentStatus.ACTIVE;
    }
}
