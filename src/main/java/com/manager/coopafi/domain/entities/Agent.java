package com.manager.coopafi.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "tb_agent")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Agent implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

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
    }
}
