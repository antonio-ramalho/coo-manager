package com.manager.coomanager.infrastructure.multitenancy;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

/**
 * Aspecto AOP que ativa automaticamente o filtro 'tenantFilter' do Hibernate
 * para a transação atual sempre que um método de serviço é invocado,
 * utilizando o identificador armazenado no TenantContextHolder.
 */
@Aspect
@Component
public class TenantFilterAspect {

    @PersistenceContext
    private EntityManager entityManager;

    @Before("@within(org.springframework.transaction.annotation.Transactional) || @annotation(org.springframework.transaction.annotation.Transactional)")
    public void enableTenantFilter() {
        Long tenantId = TenantContextHolder.getTenantId();
        if (tenantId != null) {
            Session session = entityManager.unwrap(Session.class);
            session.enableFilter("tenantFilter").setParameter("cooperativeId", tenantId);
        }
    }
}

