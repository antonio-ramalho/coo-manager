package com.manager.coomanager.iamModule.security;

import com.manager.coomanager.cooperativeModule.entities.Cooperative;
import com.manager.coomanager.shared.domain.valueobjects.*;
import com.manager.coomanager.shared.domain.person.enums.BrazilianState;
import com.manager.coomanager.iamModule.entities.Role;
import com.manager.coomanager.iamModule.entities.User;
import com.manager.coomanager.iamModule.enums.Permission;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserPrincipalTest {

    @Test
    @DisplayName("Deve construir UserPrincipal mapeando roles com prefixo ROLE_ e permissions diretamente")
    void shouldCreateUserPrincipalWithAuthorities() {
        Cooperative coop = new Cooperative(
                new Address(new Cep("56300-000"), "Rua B", "Centro", "Petrolina", "10", BrazilianState.PE),
                new Email("coop@teste.com"),
                new Phone("87999998888"),
                LocalDate.of(2020, 1, 1),
                new Cnpj("12.345.678/0001-95"),
                "Coop Teste LTDA",
                "Coop Teste",
                new StateRegistration("123456789")
        );
        ReflectionTestUtils.setField(coop, "id", 1L);

        User user = new User("Ana Clara", new Email("ana@coop.com"), new PasswordHash("hashSenha123"), coop);
        ReflectionTestUtils.setField(user, "id", 20L);

        Role roleAdmin = new Role("Administrador", "Acesso total", coop, false);
        roleAdmin.setPermissions(Set.of(Permission.USER_MANAGE, Permission.CLIENT_READ));
        user.addRole(roleAdmin);

        UserPrincipal principal = UserPrincipal.create(user);

        assertThat(principal).isNotNull();
        assertThat(principal.getUsername()).isEqualTo("ana@coop.com");
        assertThat(principal.getCooperativeId()).isEqualTo(1L);
        assertThat(principal.getCooperativeName()).isEqualTo("Coop Teste");

        Set<String> authorityStrings = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(java.util.stream.Collectors.toSet());

        assertThat(authorityStrings).contains("ROLE_ADMINISTRADOR", "USER_MANAGE", "CLIENT_READ");
    }
}

