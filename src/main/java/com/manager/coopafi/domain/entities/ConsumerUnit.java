package com.manager.coopafi.domain.entities;

import com.manager.coopafi.domain.valueObjects.Address;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    @Embedded
    private Address deliveryAddress;

    @OneToOne
    @JoinColumn(name = "jur_person")
    private JuridicPerson juridicPerson;

    @OneToMany(mappedBy = "consumerUnit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Agent> agents = new ArrayList<>();

    @OneToMany(mappedBy = "consumer")
    private List<ContractConsumer> contractConsumers = new ArrayList<>();

    public ConsumerUnit(Agent agent, Address deliveryAddress, JuridicPerson  juridicPerson) {
        addAgent(agent);
        this.deliveryAddress = deliveryAddress;
        this.juridicPerson = juridicPerson;
    }

    public void addAgent(Agent agent) {
        this.agents.add(agent);
        agent.assignConsumerUnit(this);
    }

    public void removeAgent(Agent agent) {
        this.agents.remove(agent);
        agent.assignConsumerUnit(null);
    }

    public void updateDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = Objects.requireNonNull(deliveryAddress);
    }
}
