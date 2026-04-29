package com.manager.coopafi.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.manager.coopafi.enums.DocumentStatus;
import com.manager.coopafi.exceptions.DomainException;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Table(name = "tb_organic_certificates")
@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrganicCertificate implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Enumerated(EnumType.STRING)
    private DocumentStatus documentStatus;

    private String certificateNumber;
    private LocalDate expirationDate;
    private String institutionName;

    @JsonIgnore
    @OneToMany(mappedBy = "certificate", cascade = CascadeType.PERSIST)
    private List<Farmer> farmers = new ArrayList<>();

    public OrganicCertificate(String certificateNumber, LocalDate expirationDate, String institutionName) {
        validateExpiration(expirationDate);
        this.certificateNumber = certificateNumber;
        this.expirationDate = expirationDate;
        this.institutionName = institutionName;
        this.documentStatus = DocumentStatus.ACTIVE;
    }

    private void validateExpiration(LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new DomainException("Não é possível registrar um certificado vencido.");
        }
    }

    public boolean isValid() {
        if (this.documentStatus != DocumentStatus.ACTIVE) {
            return false;
        }
        return expirationDate.isAfter(LocalDate.now());
    }

    public void updateStatusByDate() {
        if (expirationDate.isBefore(LocalDate.now())) {
            this.documentStatus = DocumentStatus.EXPIRED;
        }
    }

    public void addFarmer(Farmer farmer) {
        this.farmers.add(farmer);
        farmer.linkCertificate(this);
    }

    public void removeFarmer(Farmer farmer) {
        this.farmers.remove(farmer);
        farmer.linkCertificate(null);
    }

    public void renewDocument(LocalDate newExpirationDate) {
        expirationDate = newExpirationDate;
        this.documentStatus = DocumentStatus.ACTIVE;
    }

    public void suspendDocument() {
        this.documentStatus = DocumentStatus.SUSPENDED;
    }
}
