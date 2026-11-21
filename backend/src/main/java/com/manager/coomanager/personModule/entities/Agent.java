package com.manager.coomanager.personModule.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.manager.coomanager.shared.domain.entities.TenantEntity;
import org.hibernate.annotations.Filter;
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
@Filter(name = "tenantFilter", condition = "cooperative_id = :cooperativeId")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Agent extends TenantEntity implements Serializable {

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
        super();
        this.cargo = cargo;
        this.natPerson = natPerson;
        this.uuid = new UID();
        if (this.getCooperativeId() == null && natPerson != null && natPerson.getCooperativeId() != null) {
            this.setCooperativeId(natPerson.getCooperativeId());
        }
    }

    public void changeCargo(String cargo) {
        this.cargo = Objects.requireNonNull(cargo);
    }

    protected void assignConsumerUnit(ConsumerUnit unit) {
        this.consumerUnit = unit;
    }
}
