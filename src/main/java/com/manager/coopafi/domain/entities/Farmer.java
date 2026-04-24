package com.manager.coopafi.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tb_farmer")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Farmer implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    @OneToOne
    @JoinColumn(name = "nat_person")
    private NaturalPerson person;

    @ManyToOne
    @JoinColumn(name = "caf_id")
    private Caf caf;

    @ManyToOne
    @JoinColumn(name = "certificate_id")
    private OrganicCertificate certificate;

    @JsonIgnore
    @OneToMany(mappedBy = "farmer", cascade = CascadeType.PERSIST)
    private List<InputPurchase> inputPurchases = new ArrayList<>();

    public Farmer(NaturalPerson person) {
        this.person = Objects.requireNonNull(person);
    }

    public void addInputPurchase(InputPurchase inputPurchase) {
        this.inputPurchases.add(inputPurchase);
        inputPurchase.setFarmer(this);
    }

    public void removeInputPurchase(InputPurchase inputPurchase) {
        this.inputPurchases.remove(inputPurchase);
        inputPurchase.setFarmer(null);
    }

    public void linkCaf(Caf caf) {
        this.caf = Objects.requireNonNull(caf);
    }

    public void linkCertificate(OrganicCertificate certificate) {
        this.certificate = Objects.requireNonNull(certificate);
    }
}
