package com.manager.coomanager.iamModule.security;

import com.manager.coomanager.cooperativeModule.entities.Cooperative;
import com.manager.coomanager.shared.domain.valueobjects.*;
import com.manager.coomanager.shared.domain.person.enums.BrazilianState;
import com.manager.coomanager.iamModule.entities.Role;
import com.manager.coomanager.iamModule.entities.User;
import com.manager.coomanager.iamModule.enums.Permission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private static final String SECRET = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    private static final long EXPIRATION_MS = 3600000L; // 1 hora

    private JwtService jwtService;
    private User sampleUser;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(SECRET, EXPIRATION_MS);

        Cooperative coop = new Cooperative(
                new Address(new Cep("56300-000"), "Rua A", "Centro", "Petrolina", "10", BrazilianState.PE),
                new Email("coop@teste.com"),
                new Phone("87999998888"),
                LocalDate.of(2020, 1, 1),
                new Cnpj("12.345.678/0001-95"),
                "Coop Teste LTDA",
                "Coop Teste",
                new StateRegistration("123456789")
        );
        ReflectionTestUtils.setField(coop, "id", 100L);

        sampleUser = new User(
                "Maria Clara",
                new Email("maria@coop.com"),
                new PasswordHash("senhaCriptografada123"),
                coop
        );
        ReflectionTestUtils.setField(sampleUser, "id", 50L);

        Role role = new Role("Gestor Comercial", "Acesso a vendas", coop, false);
        role.setPermissions(Set.of(Permission.SALES_READ, Permission.SALES_WRITE));
        sampleUser.addRole(role);
    }

    @Test
    @DisplayName("Deve emitir token de acesso completo com claims de tenant, usuário e permissões")
    void shouldGenerateValidAccessToken() {
        String token = jwtService.generateAccessToken(sampleUser);

        assertThat(token).isNotBlank();
        assertThat(jwtService.validateToken(token)).isTrue();
        assertThat(jwtService.isPreAuthToken(token)).isFalse();
        assertThat(jwtService.extractEmail(token)).isEqualTo("maria@coop.com");
        assertThat(jwtService.extractUserId(token)).isEqualTo(50L);
        assertThat(jwtService.extractCooperativeId(token)).isEqualTo(100L);
    }

    @Test
    @DisplayName("Deve emitir token pre-auth com escopo restrito para 2FA")
    void shouldGeneratePreAuthToken() {
        String preAuthToken = jwtService.generatePreAuthToken(sampleUser);

        assertThat(preAuthToken).isNotBlank();
        assertThat(jwtService.validateToken(preAuthToken)).isTrue();
        assertThat(jwtService.isPreAuthToken(preAuthToken)).isTrue();
        assertThat(jwtService.extractEmail(preAuthToken)).isEqualTo("maria@coop.com");
        assertThat(jwtService.extractScope(preAuthToken)).isEqualTo(JwtService.SCOPE_PRE_AUTH);
    }

    @Test
    @DisplayName("Deve invalidar token malformado ou adulterado")
    void shouldRejectInvalidToken() {
        assertThat(jwtService.validateToken("token.invalido.aqui")).isFalse();
        assertThat(jwtService.validateToken("")).isFalse();
    }
}

