package com.manager.coomanager.shared.domain.entities;

import com.manager.coomanager.infrastructure.multitenancy.TenantContextHolder;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import java.io.Serial;
import java.io.Serializable;

@MappedSuperclass
@FilterDef(name = "tenantFilter", parameters = @ParamDef(name = "cooperativeId", type = Long.class))
@Filter(name = "tenantFilter", condition = "cooperative_id = :cooperativeId")
@Getter
@Setter
public abstract class TenantEntity implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        @Column(name = "cooperative_id", updatable = false)
        private Long cooperativeId;

        public TenantEntity() {
                Long tenantId = TenantContextHolder.getTenantId();
                if (tenantId != null) {
                        this.cooperativeId = tenantId;
                }
        }

        @PrePersist
        public void prePersistTenant() {
                if (this.cooperativeId == null) {
                        Long tenantId = TenantContextHolder.getTenantId();
                        if (tenantId != null) {
                                this.cooperativeId = tenantId;
                        }
                }
        }
}
