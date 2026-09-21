package com.manager.coopafi.personModule.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.io.Serial;
import java.io.Serializable;
import java.rmi.server.UID;
import java.util.Objects;

@Entity
@Table(name = "tb_agent")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Agent implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Column(updatable = false, nullable = false, unique = true)
    private UID uuid;

    private String cargo;

    @OneToOne
    @JoinColumn(name = "nat_person")
    private NaturalPerson natPerson;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "consumerUnit_id")
    private ConsumerUnit consumerUnit;

    public Agent(String cargo, NaturalPerson natPerson) {
        this.cargo = cargo;
        this.natPerson = natPerson;
        this.uuid = new UID();
    }

    public void changeCargo(String cargo) {
        this.cargo = Objects.requireNonNull(cargo);
    }

    protected void assignConsumerUnit(ConsumerUnit unit) {
        this.consumerUnit = unit;
    }
}
