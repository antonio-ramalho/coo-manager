package com.manager.coopafi.personModule.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.manager.coopafi.infrastructure.valueObjects.ExpirationDate;
import com.manager.coopafi.personModule.enums.DocumentStatus;
import com.manager.coopafi.infrastructure.exceptions.DomainException;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    private Long id;

    @EqualsAndHashCode.Include
    @Column(nullable = false, updatable = false, unique = true)
    private UUID uuid;

    @Enumerated(EnumType.STRING)
    private DocumentStatus documentStatus;

    private String certificateNumber;

    private ExpirationDate expirationDate;

    private String institutionName;

    @JsonIgnore
    @OneToMany(mappedBy = "certificate", cascade = CascadeType.PERSIST, orphanRemoval = false)
    private List<Farmer> farmers = new ArrayList<>();

    public OrganicCertificate(String certificateNumber, ExpirationDate expirationDate, String institutionName) {
        this.certificateNumber = certificateNumber;
        this.expirationDate = expirationDate;
        this.institutionName = institutionName;
        this.documentStatus = DocumentStatus.ACTIVE;
        this.uuid = UUID.randomUUID();
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
        return this.expirationDate.isValid();
    }

    public void updateStatusByDate() {
        if (this.expirationDate.isExpired()) {
            this.documentStatus = DocumentStatus.EXPIRED;
        }
    }

    public void suspendDocument() {
        this.documentStatus = DocumentStatus.SUSPENDED;
    }

    public void addFarmer(Farmer farmer) {
        this.farmers.add(farmer);
        farmer.linkCertificate(this);
    }

    public void removeFarmer(Farmer farmer) {
        this.farmers.remove(farmer);
        farmer.linkCertificate(null);
    }

    public void renewDocument(ExpirationDate newExpirationDate) {
        this.expirationDate = newExpirationDate;
        this.documentStatus = DocumentStatus.ACTIVE;
    }
}
