package com.manager.coomanager.iamModule.services;

import com.manager.coomanager.cooperativeModule.entities.Cooperative;
import com.manager.coomanager.shared.domain.exceptions.DomainException;
import com.manager.coomanager.iamModule.security.JwtService;
import com.manager.coomanager.iamModule.security.TotpService;
import com.manager.coomanager.shared.domain.valueobjects.*;
import com.manager.coomanager.personModule.enums.BrazilianState;
import com.manager.coomanager.iamModule.dto.LoginRequestDto;
import com.manager.coomanager.iamModule.dto.LoginResponseDto;
import com.manager.coomanager.iamModule.dto.TotpVerifyRequestDto;
import com.manager.coomanager.iamModule.entities.User;
import com.manager.coomanager.iamModule.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuthServiceTest {

    private InMemoryUserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private TotpService totpService;
    private AuthService authService;
    private User activeUser;

    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
        passwordEncoder = new BCryptPasswordEncoder();
        jwtService = new JwtService("404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970", 3600000L);
        totpService = new TotpService();
        authService = new AuthService(userRepository, passwordEncoder, jwtService, totpService);

        Cooperative coop = new Cooperative(
                new Address(new Cep("56300-000"), "Rua A", "Centro", "Petrolina", "10", BrazilianState.PE),
                new Email("coop@ativa.com"),
                new Phone("87999998888"),
                LocalDate.of(2020, 1, 1),
                new Cnpj("12.345.678/0001-95"),
                "Coop Ativa LTDA",
                "Coop Ativa",
                new StateRegistration("123456789"));
        ReflectionTestUtils.setField(coop, "id", 1L);

        activeUser = new User(
                "Carlos Silva",
                new Email("carlos@coop.com"),
                new PasswordHash(passwordEncoder.encode("senhaValida123")),
                coop);
        ReflectionTestUtils.setField(activeUser, "id", 10L);
        userRepository.save(activeUser);
    }

    @Test
    @DisplayName("Deve autenticar com sucesso e emitir accessToken quando 2FA estiver desligado")
    void shouldLoginSuccessfullyWithoutTwoFactor() {
        LoginRequestDto dto = new LoginRequestDto("carlos@coop.com", "senhaValida123");

        LoginResponseDto response = authService.login(dto);

        assertThat(response).isNotNull();
        assertThat(response.is2faRequired()).isFalse();
        assertThat(response.preAuthToken()).isNull();
        assertThat(response.accessToken()).isNotBlank();
        assertThat(response.user()).isNotNull();
        assertThat(response.user().email()).isEqualTo("carlos@coop.com");
    }

    @Test
    @DisplayName("Deve retornar preAuthToken e is2faRequired=true quando o usuário tiver 2FA habilitado")
    void shouldReturnPreAuthTokenWhenTwoFactorEnabled() {
        activeUser.enableTwoFactor("JBSWY3DPEHPK3PXP");
        userRepository.save(activeUser);

        LoginRequestDto dto = new LoginRequestDto("carlos@coop.com", "senhaValida123");

        LoginResponseDto response = authService.login(dto);

        assertThat(response).isNotNull();
        assertThat(response.is2faRequired()).isTrue();
        assertThat(response.preAuthToken()).isNotBlank();
        assertThat(jwtService.isPreAuthToken(response.preAuthToken())).isTrue();
        assertThat(response.accessToken()).isNull();
    }

    @Test
    @DisplayName("Deve lançar BadCredentialsException quando senha estiver incorreta")
    void shouldThrowExceptionWhenPasswordIncorrect() {
        LoginRequestDto dto = new LoginRequestDto("carlos@coop.com", "senhaErrada999");

        assertThatThrownBy(() -> authService.login(dto))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("E-mail ou senha incorretos");
    }

    @Test
    @DisplayName("Deve lançar DomainException quando conta do usuário estiver inativa")
    void shouldThrowExceptionWhenUserInactive() {
        activeUser.deactivate();
        userRepository.save(activeUser);

        LoginRequestDto dto = new LoginRequestDto("carlos@coop.com", "senhaValida123");

        assertThatThrownBy(() -> authService.login(dto))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("A conta do usuário não está ativa");
    }

    @Test
    @DisplayName("Deve rejeitar tentativa de 2FA com token expirado ou malformado")
    void shouldRejectTwoFactorWithInvalidPreAuthToken() {
        TotpVerifyRequestDto verifyDto = new TotpVerifyRequestDto("token.invalido", "123456");

        assertThatThrownBy(() -> authService.verifyTwoFactor(verifyDto))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("Sessão de autenticação em duas etapas expirada ou inválida");
    }

    @Test
    @DisplayName("Deve rejeitar tentativa de 2FA com código TOTP incorreto")
    void shouldRejectTwoFactorWithIncorrectTotpCode() {
        activeUser.enableTwoFactor("JBSWY3DPEHPK3PXP");
        userRepository.save(activeUser);

        String preAuthToken = jwtService.generatePreAuthToken(activeUser);
        TotpVerifyRequestDto verifyDto = new TotpVerifyRequestDto(preAuthToken, "000000");

        assertThatThrownBy(() -> authService.verifyTwoFactor(verifyDto))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("Código 2FA incorreto ou expirado");
    }

    private static class InMemoryUserRepository implements UserRepository {
        private final Map<Long, User> database = new HashMap<>();

        @Override
        public Optional<User> findByEmail(Email email) {
            return database.values().stream()
                    .filter(u -> u.getEmail() != null && u.getEmail().equals(email))
                    .findFirst();
        }

        @Override
        public Optional<User> findByEmailWithCooperative(Email email) {
            return findByEmail(email);
        }

        @Override
        public boolean existsByEmail(Email email) {
            return findByEmail(email).isPresent();
        }

        @Override
        public Page<User> findAllByCooperativeId(Long cooperativeId, Pageable pageable) {
            return Page.empty();
        }

        @Override
        public Optional<User> findByIdAndCooperativeId(Long id, Long cooperativeId) {
            return Optional.empty();
        }

        @Override
        public Page<User> searchByCooperativeAndTerm(Long cooperativeId, String searchTerm, Pageable pageable) {
            return Page.empty();
        }

        @Override
        public <S extends User> S save(S entity) {
            database.put(entity.getId(), entity);
            return entity;
        }

        @Override
        public Optional<User> findById(Long aLong) {
            return Optional.ofNullable(database.get(aLong));
        }

        @Override
        public boolean existsById(Long aLong) {
            return database.containsKey(aLong);
        }

        @Override
        public List<User> findAll() {
            return new ArrayList<>(database.values());
        }

        @Override
        public List<User> findAllById(Iterable<Long> longs) {
            return List.of();
        }

        @Override
        public long count() {
            return database.size();
        }

        @Override
        public void deleteById(Long aLong) {
            database.remove(aLong);
        }

        @Override
        public void delete(User entity) {
            database.remove(entity.getId());
        }

        @Override
        public void deleteAllById(Iterable<? extends Long> longs) {
        }

        @Override
        public void deleteAll(Iterable<? extends User> entities) {
        }

        @Override
        public void deleteAll() {
        }

        @Override
        public <S extends User> List<S> saveAll(Iterable<S> entities) {
            return List.of();
        }

        @Override
        public void flush() {
        }

        @Override
        public <S extends User> S saveAndFlush(S entity) {
            return save(entity);
        }

        @Override
        public <S extends User> List<S> saveAllAndFlush(Iterable<S> entities) {
            return List.of();
        }

        @Override
        public void deleteAllInBatch(Iterable<User> entities) {
        }

        @Override
        public void deleteAllByIdInBatch(Iterable<Long> longs) {
        }

        @Override
        public void deleteAllInBatch() {
        }

        @Override
        public User getOne(Long aLong) {
            return null;
        }

        @Override
        public User getById(Long aLong) {
            return null;
        }

        @Override
        public User getReferenceById(Long aLong) {
            return null;
        }

        @Override
        public <S extends User> Optional<S> findOne(Example<S> example) {
            return Optional.empty();
        }

        @Override
        public <S extends User> List<S> findAll(Example<S> example) {
            return List.of();
        }

        @Override
        public <S extends User> List<S> findAll(Example<S> example, Sort sort) {
            return List.of();
        }

        @Override
        public <S extends User> Page<S> findAll(Example<S> example, Pageable pageable) {
            return Page.empty();
        }

        @Override
        public <S extends User> long count(Example<S> example) {
            return 0;
        }

        @Override
        public <S extends User> boolean exists(Example<S> example) {
            return false;
        }

        @Override
        public <S extends User, R> R findBy(Example<S> example,
                Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
            return null;
        }

        @Override
        public List<User> findAll(Sort sort) {
            return List.of();
        }

        @Override
        public Page<User> findAll(Pageable pageable) {
            return Page.empty();
        }
    }
}
