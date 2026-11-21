package com.manager.coomanager.iamModule.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PermissionTest {

    @Test
    @DisplayName("Deve verificar comportamento polimórfico de permissões de leitura")
    void shouldValidateReadPermissions() {
        Permission clientRead = Permission.CLIENT_READ;
        assertThat(clientRead.canRead()).isTrue();
        assertThat(clientRead.canWrite()).isFalse();
        assertThat(clientRead.isAdministrative()).isFalse();
        assertThat(clientRead.getModule()).isEqualTo("clients");
    }

    @Test
    @DisplayName("Deve verificar comportamento polimórfico de permissões de escrita")
    void shouldValidateWritePermissions() {
        Permission clientWrite = Permission.CLIENT_WRITE;
        assertThat(clientWrite.canRead()).isTrue();
        assertThat(clientWrite.canWrite()).isTrue();
        assertThat(clientWrite.isAdministrative()).isFalse();
        assertThat(clientWrite.getModule()).isEqualTo("clients");
    }

    @Test
    @DisplayName("Deve verificar comportamento polimórfico de permissão administrativa")
    void shouldValidateAdministrativePermissions() {
        Permission userManage = Permission.USER_MANAGE;
        assertThat(userManage.canRead()).isTrue();
        assertThat(userManage.canWrite()).isTrue();
        assertThat(userManage.isAdministrative()).isTrue();
        assertThat(userManage.getModule()).isEqualTo("users");
    }
}

