package com.manager.coomanager.infrastructure.multitenancy;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TenantEntityTest {

    private static class DummyTenantEntity extends TenantEntity {
    }

    @AfterEach
    void tearDown() {
        TenantContextHolder.clear();
    }

    @Test
    @DisplayName("Deve inicializar cooperativeId no construtor quando TenantContextHolder estiver definido")
    void shouldInitializeCooperativeIdFromContextHolderInConstructor() {
        TenantContextHolder.setTenantId(15L);

        DummyTenantEntity entity = new DummyTenantEntity();

        assertThat(entity.getCooperativeId()).isEqualTo(15L);
    }

    @Test
    @DisplayName("Deve preencher cooperativeId via prePersistTenant quando cooperativeId for nulo")
    void shouldSetCooperativeIdOnPrePersistWhenNull() {
        DummyTenantEntity entity = new DummyTenantEntity();
        assertThat(entity.getCooperativeId()).isNull();

        TenantContextHolder.setTenantId(99L);
        entity.prePersistTenant();

        assertThat(entity.getCooperativeId()).isEqualTo(99L);
    }

    @Test
    @DisplayName("Não deve sobrescrever cooperativeId existente no prePersistTenant")
    void shouldNotOverwriteExistingCooperativeIdOnPrePersist() {
        DummyTenantEntity entity = new DummyTenantEntity();
        entity.setCooperativeId(7L);

        TenantContextHolder.setTenantId(999L);
        entity.prePersistTenant();

        assertThat(entity.getCooperativeId()).isEqualTo(7L);
    }
}

