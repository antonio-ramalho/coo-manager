package com.manager.coomanager.iamModule.entities;

import com.manager.coomanager.cooperativeModule.entities.Cooperative;
import com.manager.coomanager.shared.domain.valueobjects.*;
import com.manager.coomanager.shared.domain.person.enums.BrazilianState;
import com.manager.coomanager.shared.domain.enums.Status;
import com.manager.coomanager.iamModule.enums.Permission;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserEntityTest {

    private Cooperative createSampleCooperative() {
        return new Cooperative(
                new Address(new Cep("56300-000"), "Rua das Palmeiras", "Centro", "Petrolina", "100", BrazilianState.PE),
                new Email("contato@coop.com"),
                new Phone("87988887777"),
                LocalDate.of(2020, 1, 1),
                new Cnpj("12.345.678/0001-95"),
                "Coop Legal LTDA",
                "Coop Legal",
                new StateRegistration("1234567890")
        );
    }

    @Test
    @DisplayName("Deve verificar permissões do usuário dinamicamente através de seus cargos")
    void shouldCheckPermissionsThroughRoles() {
        Cooperative coop = createSampleCooperative();
        User user = new User("Maria da Silva", new Email("maria@coop.com"), new PasswordHash("senhaForte123"), coop);

        Role roleEstoque = new Role("Operador de Estoque", "Acesso aos insumos", coop, false);
        roleEstoque.setPermissions(Set.of(Permission.INPUT_READ, Permission.INPUT_WRITE));

        user.addRole(roleEstoque);

        assertThat(user.hasPermission(Permission.INPUT_READ)).isTrue();
        assertThat(user.hasPermission(Permission.INPUT_WRITE)).isTrue();
        assertThat(user.hasPermission(Permission.CLIENT_WRITE)).isFalse();
        assertThat(user.hasPermission(Permission.USER_MANAGE)).isFalse();
    }

    @Test
    @DisplayName("Deve ativar, desativar e gerenciar 2FA no usuário")
    void shouldManageUserStatusAndTwoFactor() {
        Cooperative coop = createSampleCooperative();
        User user = new User("João Santos", new Email("joao@coop.com"), new PasswordHash("senhaForte123"), coop);

        assertThat(user.getStatus()).isEqualTo(Status.ACTIVE);
        assertThat(user.isTwoFactorEnabled()).isFalse();

        user.enableTwoFactor("JBSWY3DPEHPK3PXP");
        assertThat(user.isTwoFactorEnabled()).isTrue();
        assertThat(user.getTwoFactorSecret()).isEqualTo("JBSWY3DPEHPK3PXP");

        user.disableTwoFactor();
        assertThat(user.isTwoFactorEnabled()).isFalse();
        assertThat(user.getTwoFactorSecret()).isNull();

        user.deactivate();
        assertThat(user.getStatus()).isEqualTo(Status.INACTIVE);

        user.activate();
        assertThat(user.getStatus()).isEqualTo(Status.ACTIVE);
    }
}

