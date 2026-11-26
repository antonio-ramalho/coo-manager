package com.manager.coomanager.infrastructure.multitenancy;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TenantContextHolderTest {

    @AfterEach
    void tearDown() {
        TenantContextHolder.clear();
    }

    @Test
    @DisplayName("Deve definir, recuperar e limpar o ID do tenant na thread atual")
    void shouldSetGetAndClearTenantId() {
        assertThat(TenantContextHolder.getTenantId()).isNull();

        TenantContextHolder.setTenantId(42L);
        assertThat(TenantContextHolder.getTenantId()).isEqualTo(42L);

        TenantContextHolder.clear();
        assertThat(TenantContextHolder.getTenantId()).isNull();
    }
}

