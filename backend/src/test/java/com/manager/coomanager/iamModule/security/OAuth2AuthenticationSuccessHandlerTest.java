package com.manager.coomanager.iamModule.security;

import com.manager.coomanager.cooperativeModule.entities.Cooperative;
import com.manager.coomanager.shared.domain.valueobjects.*;
import com.manager.coomanager.shared.domain.person.enums.BrazilianState;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

class OAuth2AuthenticationSuccessHandlerTest {

    private InMemoryUserRepository userRepository;
    private JwtService jwtService;
    private OAuth2AuthenticationSuccessHandler handler;
    private Cooperative activeCoop;
    private User registeredUser;

    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
        jwtService = new JwtService("404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970", 3600000L);
        handler = new OAuth2AuthenticationSuccessHandler(userRepository, jwtService);
        ReflectionTestUtils.setField(handler, "allowedOrigins", "http://localhost:4200");

        activeCoop = new Cooperative(
                new Address(new Cep("56300-000"), "Rua B", "Centro", "Petrolina", "10", BrazilianState.PE),
                new Email("coop@teste.com"),
                new Phone("87999998888"),
                LocalDate.of(2020, 1, 1),
                new Cnpj("12.345.678/0001-95"),
                "Coop Teste LTDA",
                "Coop Teste",
                new StateRegistration("123456789")
        );
        ReflectionTestUtils.setField(activeCoop, "id", 1L);

        registeredUser = new User("Pedro Alcantara", new Email("pedro@gmail.com"), null, activeCoop);
        ReflectionTestUtils.setField(registeredUser, "id", 100L);
        userRepository.save(registeredUser);
    }

    private Authentication createMockAuth(String email) {
        OAuth2User oAuth2User = new DummyOAuth2User(Map.of("email", email, "name", "Nome Teste"));
        return new DummyAuthentication(oAuth2User);
    }

    @Test
    @DisplayName("Deve rejeitar login OAuth2 quando e-mail não tiver pré-cadastro (sem open-registration)")
    void shouldDenyAccessWhenEmailNotRegistered() {
        Authentication auth = createMockAuth("desconhecido@gmail.com");

        String targetUrl = handler.determineTargetUrl(auth);

        assertThat(targetUrl).contains("error=access_denied");
        assertThat(targetUrl).contains("Acesso+negado");
    }

    @Test
    @DisplayName("Deve emitir accessToken completo quando e-mail estiver cadastrado e sem 2FA")
    void shouldIssueAccessTokenForRegisteredUserWithoutTwoFactor() {
        Authentication auth = createMockAuth("pedro@gmail.com");

        String targetUrl = handler.determineTargetUrl(auth);

        assertThat(targetUrl).contains("token=");
        assertThat(targetUrl).doesNotContain("twoFactorRequired");
    }

    @Test
    @DisplayName("Deve emitir preAuthToken e sinalizar twoFactorRequired quando usuário cadastrado tiver 2FA ativo")
    void shouldIssuePreAuthTokenForUserWithTwoFactor() {
        registeredUser.enableTwoFactor("JBSWY3DPEHPK3PXP");
        userRepository.save(registeredUser);

        Authentication auth = createMockAuth("pedro@gmail.com");

        String targetUrl = handler.determineTargetUrl(auth);

        assertThat(targetUrl).contains("twoFactorRequired=true");
        assertThat(targetUrl).contains("preAuthToken=");
    }

    private static class DummyOAuth2User implements OAuth2User {
        private final Map<String, Object> attributes;
        DummyOAuth2User(Map<String, Object> attributes) { this.attributes = attributes; }
        @Override public Map<String, Object> getAttributes() { return attributes; }
        @Override public Collection<? extends org.springframework.security.core.GrantedAuthority> getAuthorities() { return List.of(); }
        @Override public String getName() { return (String) attributes.get("name"); }
    }

    private static class DummyAuthentication implements Authentication {
        private final OAuth2User principal;
        DummyAuthentication(OAuth2User principal) { this.principal = principal; }
        @Override public Collection<? extends org.springframework.security.core.GrantedAuthority> getAuthorities() { return List.of(); }
        @Override public Object getCredentials() { return null; }
        @Override public Object getDetails() { return null; }
        @Override public Object getPrincipal() { return principal; }
        @Override public boolean isAuthenticated() { return true; }
        @Override public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {}
        @Override public String getName() { return principal.getName(); }
    }

    private static class InMemoryUserRepository implements UserRepository {
        private final Map<Long, User> database = new HashMap<>();
        @Override public Optional<User> findByEmail(Email email) {
            return database.values().stream()
                    .filter(u -> u.getEmail() != null && u.getEmail().equals(email))
                    .findFirst();
        }
        @Override public Optional<User> findByEmailWithCooperative(Email email) { return findByEmail(email); }
        @Override public boolean existsByEmail(Email email) { return findByEmail(email).isPresent(); }
        @Override public Page<User> findAllByCooperativeId(Long cooperativeId, Pageable pageable) { return Page.empty(); }
        @Override public Optional<User> findByIdAndCooperativeId(Long id, Long cooperativeId) { return Optional.empty(); }
        @Override public Page<User> searchByCooperativeAndTerm(Long cooperativeId, String searchTerm, Pageable pageable) { return Page.empty(); }
        @Override public <S extends User> S save(S entity) {
            database.put(entity.getId(), entity);
            return entity;
        }
        @Override public Optional<User> findById(Long aLong) { return Optional.ofNullable(database.get(aLong)); }
        @Override public boolean existsById(Long aLong) { return database.containsKey(aLong); }
        @Override public List<User> findAll() { return new ArrayList<>(database.values()); }
        @Override public List<User> findAllById(Iterable<Long> longs) { return List.of(); }
        @Override public long count() { return database.size(); }
        @Override public void deleteById(Long aLong) { database.remove(aLong); }
        @Override public void delete(User entity) { database.remove(entity.getId()); }
        @Override public void deleteAllById(Iterable<? extends Long> longs) {}
        @Override public void deleteAll(Iterable<? extends User> entities) {}
        @Override public void deleteAll() {}
        @Override public <S extends User> List<S> saveAll(Iterable<S> entities) { return List.of(); }
        @Override public void flush() {}
        @Override public <S extends User> S saveAndFlush(S entity) { return save(entity); }
        @Override public <S extends User> List<S> saveAllAndFlush(Iterable<S> entities) { return List.of(); }
        @Override public void deleteAllInBatch(Iterable<User> entities) {}
        @Override public void deleteAllByIdInBatch(Iterable<Long> longs) {}
        @Override public void deleteAllInBatch() {}
        @Override public User getOne(Long aLong) { return null; }
        @Override public User getById(Long aLong) { return null; }
        @Override public User getReferenceById(Long aLong) { return null; }
        @Override public <S extends User> Optional<S> findOne(Example<S> example) { return Optional.empty(); }
        @Override public <S extends User> List<S> findAll(Example<S> example) { return List.of(); }
        @Override public <S extends User> List<S> findAll(Example<S> example, Sort sort) { return List.of(); }
        @Override public <S extends User> Page<S> findAll(Example<S> example, Pageable pageable) { return Page.empty(); }
        @Override public <S extends User> long count(Example<S> example) { return 0; }
        @Override public <S extends User> boolean exists(Example<S> example) { return false; }
        @Override public <S extends User, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) { return null; }
        @Override public List<User> findAll(Sort sort) { return List.of(); }
        @Override public Page<User> findAll(Pageable pageable) { return Page.empty(); }
    }
}

