package com.manager.coopafi.domain.entities;

import com.manager.coopafi.domain.valueObjects.Address;
import com.manager.coopafi.enums.UserStatus;
import com.manager.coopafi.exceptions.DomainException;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tb_consumerUnit")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ConsumerUnit implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Embedded
    private Address deliveryAddress;

    @OneToOne
    @JoinColumn(name = "jur_person")
    private JuridicPerson juridicPerson;

    @OneToMany(mappedBy = "consumerUnit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Agent> agents = new ArrayList<>();

    @OneToMany(mappedBy = "consumer")
    private List<ContractConsumer> contractConsumers = new ArrayList<>();

    public ConsumerUnit(@NonNull List<Agent> agents, Address deliveryAddress, JuridicPerson  juridicPerson) {
        agents.forEach(this::addAgent);
        this.deliveryAddress = deliveryAddress;
        this.juridicPerson = juridicPerson;
        this.status = UserStatus.ACTIVE;
    }

    public void deactivate() {
        if (this.status == UserStatus.INACTIVE) {
            throw new DomainException("Esta Unidade Consumidora já está inativa.");
        }
        this.status = UserStatus.INACTIVE;
    }

    public void addAgent(Agent agent) {
        if (agent == null) {
            throw new DomainException("Não é possível cadastrar uma unidade sem um Agente.");
        }
        this.agents.add(agent);
        agent.assignConsumerUnit(this);
    }

    public void removeAgent(Agent agent) {
        if (agents.size() == 1) {
            throw new DomainException("Não é possível remover o único representante desta unidade.");
        }
        this.agents.remove(agent);
        agent.assignConsumerUnit(null);
    }

    public void updateDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = Objects.requireNonNull(deliveryAddress);
    }
}
