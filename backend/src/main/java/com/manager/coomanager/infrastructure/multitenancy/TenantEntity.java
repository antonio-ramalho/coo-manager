package com.manager.coomanager.infrastructure.multitenancy;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import java.io.Serial;
import java.io.Serializable;

/**
 * Superclasse mapeada para todas as entidades que devem ser isoladas por tenant (cooperativa).
 * Define o filtro do Hibernate 'tenantFilter' que injeta automaticamente a cláusula
 * WHERE cooperative_id = :cooperativeId em todas as consultas SQL geradas.
 */
@MappedSuperclass
@FilterDef(
        name = "tenantFilter",
        parameters = @ParamDef(name = "cooperativeId", type = Long.class)
)
@Filter(
        name = "tenantFilter",
        condition = "cooperative_id = :cooperativeId"
)
@Getter
@Setter
public abstract class TenantEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "cooperative_id")
    private Long cooperativeId;
}

