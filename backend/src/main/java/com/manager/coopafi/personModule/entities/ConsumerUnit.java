package com.manager.coopafi.personModule.entities;

import com.manager.coopafi.contractModule.entities.ContractConsumer;
import com.manager.coopafi.infrastructure.valueObjects.Address;
import com.manager.coopafi.personModule.enums.Status;
import com.manager.coopafi.infrastructure.exceptions.DomainException;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
    private Long id;

    @EqualsAndHashCode.Include
    @Column(updatable = false, nullable = false, unique = true)
    private UUID uuid;

    @Enumerated(EnumType.STRING)
    private Status status;

    private boolean isSubsidiaryCNPJ;

    @Embedded
    private Address deliveryAddress;

    @OneToOne
    @JoinColumn(name = "jur_person")
    private JuridicPerson juridicPerson;

    @OneToMany(mappedBy = "consumerUnit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Agent> agents = new ArrayList<>();

    @OneToMany(mappedBy = "consumer")
    private List<ContractConsumer> contractConsumers = new ArrayList<>();

    public ConsumerUnit(@NonNull List<Agent> agents, Address deliveryAddress, JuridicPerson  juridicPerson,
                        boolean isSubsidiaryCNPJ) {
        agents.forEach(this::addAgent);
        this.deliveryAddress = deliveryAddress;
        this.juridicPerson = juridicPerson;
        this.isSubsidiaryCNPJ = isSubsidiaryCNPJ;
        this.status = Status.ACTIVE;
        this.uuid = UUID.randomUUID();
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

    public void deactivate() {
        this.status = Status.INACTIVE;
    }

    public void activate() {
        this.status = Status.ACTIVE;
    }

    public void suspend() {
        this.status = Status.SUSPENDED;
    }
}
