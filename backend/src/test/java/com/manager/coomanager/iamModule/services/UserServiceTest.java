package com.manager.coomanager.iamModule.services;

import com.manager.coomanager.cooperativeModule.entities.Cooperative;
import com.manager.coomanager.cooperativeModule.repositories.CooperativeRepository;
import com.manager.coomanager.shared.domain.exceptions.DomainException;
import com.manager.coomanager.shared.domain.valueobjects.*;
import com.manager.coomanager.personModule.enums.BrazilianState;
import com.manager.coomanager.personModule.enums.Status;
import com.manager.coomanager.iamModule.dto.UserRequestDto;
import com.manager.coomanager.iamModule.dto.UserResponseDto;
import com.manager.coomanager.iamModule.dto.UserUpdateDto;
import com.manager.coomanager.iamModule.entities.Role;
import com.manager.coomanager.iamModule.entities.User;
import com.manager.coomanager.iamModule.enums.Permission;
import com.manager.coomanager.iamModule.repositories.RoleRepository;
import com.manager.coomanager.iamModule.repositories.UserRepository;
import com.manager.coomanager.iamModule.services.validators.UserCooperativeActiveValidator;
import com.manager.coomanager.iamModule.services.validators.UserEmailUniqueValidator;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import com.manager.coomanager.iamModule.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.test.util.ReflectionTestUtils;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class UserServiceTest {

    private InMemoryUserRepository userRepository;
    private InMemoryRoleRepository roleRepository;
    private InMemoryCooperativeRepository cooperativeRepository;
    private UserService service;
    private Cooperative activeCooperative;
    private Role sampleRole;

    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
        roleRepository = new InMemoryRoleRepository();
        cooperativeRepository = new InMemoryCooperativeRepository();

        UserEmailUniqueValidator emailValidator = new UserEmailUniqueValidator(userRepository);
        UserCooperativeActiveValidator coopValidator = new UserCooperativeActiveValidator(cooperativeRepository);

        service = new UserService(
                userRepository,
                roleRepository,
                cooperativeRepository,
                List.of(emailValidator, coopValidator),
                new BCryptPasswordEncoder());

        activeCooperative = new Cooperative(
                new Address(new Cep("56300-000"), "Rua A", "Centro", "Petrolina", "10", BrazilianState.PE),
                new Email("coop@ativa.com"),
                new Phone("87999998888"),
                LocalDate.of(2020, 1, 1),
                new Cnpj("12.345.678/0001-95"),
                "Coop Ativa LTDA",
                "Coop Ativa",
                new StateRegistration("123456789"));
        ReflectionTestUtils.setField(activeCooperative, "id", 1L);
        cooperativeRepository.save(activeCooperative);

        sampleRole = new Role("Assistente Financeiro", "Gestão de pagamentos", activeCooperative, false);
        sampleRole.setPermissions(Set.of(Permission.SALES_READ));
        ReflectionTestUtils.setField(sampleRole, "id", 10L);
        roleRepository.save(sampleRole);
    }

    @Test
    @DisplayName("Deve cadastrar um novo funcionário com sucesso vinculado à cooperativa e cargos")
    void shouldCreateUserSuccessfully() {
        UserRequestDto dto = new UserRequestDto(
                "Carlos Eduardo",
                "carlos@coop.com",
                "senha12345",
                1L,
                Set.of(10L));

        UserResponseDto response = service.insert(dto);

        assertThat(response).isNotNull();
        assertThat(response.id()).isNotNull();
        assertThat(response.fullName()).isEqualTo("Carlos Eduardo");
        assertThat(response.email()).isEqualTo("carlos@coop.com");
        assertThat(response.cooperativeId()).isEqualTo(1L);
        assertThat(response.roles()).hasSize(1);
    }

    @Test
    @DisplayName("Deve lançar DomainException quando tentar cadastrar funcionário com e-mail duplicado")
    void shouldThrowExceptionWhenDuplicateEmail() {
        UserRequestDto dto1 = new UserRequestDto("Carlos Eduardo", "carlos@coop.com", "senha12345", 1L, Set.of());
        service.insert(dto1);

        UserRequestDto dto2 = new UserRequestDto("Carlos Outro", "carlos@coop.com", "outraSenha123", 1L, Set.of());
        assertThatThrownBy(() -> service.insert(dto2))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Já existe um usuário cadastrado com o e-mail informado");
    }

    @Test
    @DisplayName("Deve lançar DomainException quando tentar cadastrar funcionário em cooperativa inativa")
    void shouldThrowExceptionWhenCooperativeInactive() {
        Cooperative inactiveCoop = new Cooperative(
                new Address(new Cep("56300-000"), "Rua B", "Centro", "Petrolina", "20", BrazilianState.PE),
                new Email("coop@inativa.com"),
                new Phone("87999998888"),
                LocalDate.of(2020, 1, 1),
                new Cnpj("99.888.777/0001-66"),
                "Coop Inativa LTDA",
                "Coop Inativa",
                new StateRegistration("987654321"));
        inactiveCoop.deactivate();
        ReflectionTestUtils.setField(inactiveCoop, "id", 2L);
        cooperativeRepository.save(inactiveCoop);

        UserRequestDto dto = new UserRequestDto("Ana Paula", "ana@coop.com", "senhaForte123", 2L, Set.of());
        assertThatThrownBy(() -> service.insert(dto))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Não é possível cadastrar usuários em uma cooperativa inativa");
    }

    @Test
    @DisplayName("Deve atualizar dados e cargos do funcionário com sucesso")
    void shouldUpdateUser() {
        UserRequestDto createDto = new UserRequestDto("Lucas Silva", "lucas@coop.com", "senhaForte123", 1L, Set.of());
        UserResponseDto created = service.insert(createDto);

        UserUpdateDto updateDto = new UserUpdateDto("Lucas Silva Sauro", "lucas.sauro@coop.com", Set.of(10L));
        UserResponseDto updated = service.update(created.id(), updateDto);

        assertThat(updated.fullName()).isEqualTo("Lucas Silva Sauro");
        assertThat(updated.email()).isEqualTo("lucas.sauro@coop.com");
        assertThat(updated.roles()).hasSize(1);
    }

    @Test
    @DisplayName("Deve inativar e reativar usuário com sucesso")
    void shouldActivateAndDeactivateUser() {
        UserRequestDto dto = new UserRequestDto("Marcos Paulo", "marcos@coop.com", "senhaForte123", 1L, Set.of());
        UserResponseDto created = service.insert(dto);

        service.deactivate(created.id());
        User user = userRepository.findById(created.id()).orElseThrow();
        assertThat(user.getStatus()).isEqualTo(Status.INACTIVE);

        service.activate(created.id());
        user = userRepository.findById(created.id()).orElseThrow();
        assertThat(user.getStatus()).isEqualTo(Status.ACTIVE);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Deve impedir que usuário sem USER_MANAGE atualize outro usuário quando autenticado")
    void shouldPreventNonAdminFromUpdatingOtherUserWhenAuthenticated() {
        UserRequestDto dto = new UserRequestDto("Outro Usuario", "outro@coop.com", "senhaForte123", 1L, Set.of());
        UserResponseDto created = service.insert(dto);

        UserPrincipal sellerPrincipal = new UserPrincipal(
                999L, "Vendedor", "vendedor@coop.com", "senha", 1L, "Coop", Status.ACTIVE,
                List.of(new SimpleGrantedAuthority("SALES_WRITE"), new SimpleGrantedAuthority("SALES_READ")));
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(sellerPrincipal, null, sellerPrincipal.getAuthorities()));

        UserUpdateDto updateDto = new UserUpdateDto("Tentativa de invasão", "outro@coop.com", null);
        assertThatThrownBy(() -> service.update(created.id(), updateDto))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("Acesso negado");
    }

    @Test
    @DisplayName("Deve impedir que o próprio usuário altere seus cargos (prevenção de auto-escalonamento)")
    void shouldPreventUserFromChangingOwnRolesWhenAuthenticated() {
        UserRequestDto dto = new UserRequestDto("Vendedor User", "vendedor@coop.com", "senhaForte123", 1L, Set.of());
        UserResponseDto created = service.insert(dto);

        // Caso 1: Vendedor comum tenta passar cargos
        UserPrincipal sellerPrincipal = new UserPrincipal(
                created.id(), "Vendedor User", "vendedor@coop.com", "senha", 1L, "Coop", Status.ACTIVE,
                List.of(new SimpleGrantedAuthority("SALES_WRITE")));
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(sellerPrincipal, null, sellerPrincipal.getAuthorities()));

        UserUpdateDto updateDto = new UserUpdateDto("Vendedor User", "vendedor@coop.com", Set.of(10L));
        assertThatThrownBy(() -> service.update(created.id(), updateDto))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Você não possui permissão para alterar cargos");

        // Caso 2: Usuário com USER_MANAGE tenta alterar seus próprios cargos
        UserPrincipal adminPrincipal = new UserPrincipal(
                created.id(), "Admin User", "vendedor@coop.com", "senha", 1L, "Coop", Status.ACTIVE,
                List.of(new SimpleGrantedAuthority("USER_MANAGE")));
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(adminPrincipal, null, adminPrincipal.getAuthorities()));

        assertThatThrownBy(() -> service.update(created.id(), updateDto))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Não é permitido alterar o próprio cargo");
    }

    @Test
    @DisplayName("Deve impedir que o usuário autenticado inative a própria conta")
    void shouldPreventUserFromDeactivatingOwnAccountWhenAuthenticated() {
        UserRequestDto dto = new UserRequestDto("Admin User", "admin@coop.com", "senhaForte123", 1L, Set.of());
        UserResponseDto created = service.insert(dto);

        UserPrincipal adminPrincipal = new UserPrincipal(
                created.id(), "Admin User", "admin@coop.com", "senha", 1L, "Coop", Status.ACTIVE,
                List.of(new SimpleGrantedAuthority("USER_MANAGE")));
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(adminPrincipal, null, adminPrincipal.getAuthorities()));

        assertThatThrownBy(() -> service.deactivate(created.id()))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Não é permitido inativar a própria conta");
    }

    private static class InMemoryUserRepository implements UserRepository {
        private final Map<Long, User> database = new HashMap<>();
        private long seq = 1L;

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
            List<User> list = database.values().stream()
                    .filter(u -> u.getCooperative() != null
                            && Objects.equals(u.getCooperative().getId(), cooperativeId))
                    .toList();
            return new PageImpl<>(list, pageable, list.size());
        }

        @Override
        public Optional<User> findByIdAndCooperativeId(Long id, Long cooperativeId) {
            return database.values().stream()
                    .filter(u -> Objects.equals(u.getId(), id) &&
                            u.getCooperative() != null &&
                            Objects.equals(u.getCooperative().getId(), cooperativeId))
                    .findFirst();
        }

        @Override
        public Page<User> searchByCooperativeAndTerm(Long cooperativeId, String searchTerm, Pageable pageable) {
            List<User> list = database.values().stream()
                    .filter(u -> u.getCooperative() != null
                            && Objects.equals(u.getCooperative().getId(), cooperativeId))
                    .filter(u -> searchTerm.isBlank() ||
                            u.getFullName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                            u.getEmail().getAddressEmail().toLowerCase().contains(searchTerm.toLowerCase()))
                    .toList();
            return new PageImpl<>(list, pageable, list.size());
        }

        @Override
        public <S extends User> S save(S entity) {
            if (entity.getId() == null) {
                ReflectionTestUtils.setField(entity, "id", seq++);
            }
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
            return database.get(aLong);
        }

        @Override
        public User getById(Long aLong) {
            return database.get(aLong);
        }

        @Override
        public User getReferenceById(Long aLong) {
            return database.get(aLong);
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

    private static class InMemoryRoleRepository implements RoleRepository {
        private final Map<Long, Role> database = new HashMap<>();

        @Override
        public List<Role> findByCooperativeId(Long cooperativeId) {
            return List.of();
        }

        @Override
        public List<Role> findBySystemRoleTrue() {
            return List.of();
        }

        @Override
        public List<Role> findAllAvailableForCooperative(Long cooperativeId) {
            return List.of();
        }

        @Override
        public boolean existsByNameAndCooperativeId(String name, Long cooperativeId) {
            return false;
        }

        @Override
        public Optional<Role> findByNameAndCooperativeId(String name, Long cooperativeId) {
            return Optional.empty();
        }

        @Override
        public <S extends Role> S save(S entity) {
            database.put(entity.getId(), entity);
            return entity;
        }

        @Override
        public Optional<Role> findById(Long aLong) {
            return Optional.ofNullable(database.get(aLong));
        }

        @Override
        public boolean existsById(Long aLong) {
            return database.containsKey(aLong);
        }

        @Override
        public List<Role> findAll() {
            return List.of();
        }

        @Override
        public List<Role> findAllById(Iterable<Long> longs) {
            List<Role> list = new ArrayList<>();
            for (Long id : longs) {
                if (database.containsKey(id))
                    list.add(database.get(id));
            }
            return list;
        }

        @Override
        public long count() {
            return 0;
        }

        @Override
        public void deleteById(Long aLong) {
        }

        @Override
        public void delete(Role entity) {
        }

        @Override
        public void deleteAllById(Iterable<? extends Long> longs) {
        }

        @Override
        public void deleteAll(Iterable<? extends Role> entities) {
        }

        @Override
        public void deleteAll() {
        }

        @Override
        public <S extends Role> List<S> saveAll(Iterable<S> entities) {
            return List.of();
        }

        @Override
        public void flush() {
        }

        @Override
        public <S extends Role> S saveAndFlush(S entity) {
            return null;
        }

        @Override
        public <S extends Role> List<S> saveAllAndFlush(Iterable<S> entities) {
            return List.of();
        }

        @Override
        public void deleteAllInBatch(Iterable<Role> entities) {
        }

        @Override
        public void deleteAllByIdInBatch(Iterable<Long> longs) {
        }

        @Override
        public void deleteAllInBatch() {
        }

        @Override
        public Role getOne(Long aLong) {
            return null;
        }

        @Override
        public Role getById(Long aLong) {
            return null;
        }

        @Override
        public Role getReferenceById(Long aLong) {
            return null;
        }

        @Override
        public <S extends Role> Optional<S> findOne(Example<S> example) {
            return Optional.empty();
        }

        @Override
        public <S extends Role> List<S> findAll(Example<S> example) {
            return List.of();
        }

        @Override
        public <S extends Role> List<S> findAll(Example<S> example, Sort sort) {
            return List.of();
        }

        @Override
        public <S extends Role> Page<S> findAll(Example<S> example, Pageable pageable) {
            return Page.empty();
        }

        @Override
        public <S extends Role> long count(Example<S> example) {
            return 0;
        }

        @Override
        public <S extends Role> boolean exists(Example<S> example) {
            return false;
        }

        @Override
        public <S extends Role, R> R findBy(Example<S> example,
                Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
            return null;
        }

        @Override
        public List<Role> findAll(Sort sort) {
            return List.of();
        }

        @Override
        public Page<Role> findAll(Pageable pageable) {
            return Page.empty();
        }
    }

    private static class InMemoryCooperativeRepository implements CooperativeRepository {
        private final Map<Long, Cooperative> database = new HashMap<>();

        @Override
        public boolean existsByCnpj(Cnpj cnpj) {
            return false;
        }

        @Override
        public boolean existsByEmail(Email email) {
            return false;
        }

        @Override
        public Optional<Cooperative> findByCnpj(Cnpj cnpj) {
            return Optional.empty();
        }

        @Override
        public Optional<Cooperative> findByEmail(Email email) {
            return Optional.empty();
        }

        @Override
        public Page<Cooperative> findAllBySearchTerm(String searchTerm, Pageable pageable) {
            return Page.empty();
        }

        @Override
        public <S extends Cooperative> S save(S entity) {
            database.put(entity.getId(), entity);
            return entity;
        }

        @Override
        public Optional<Cooperative> findById(Long aLong) {
            return Optional.ofNullable(database.get(aLong));
        }

        @Override
        public boolean existsById(Long aLong) {
            return database.containsKey(aLong);
        }

        @Override
        public List<Cooperative> findAll() {
            return List.of();
        }

        @Override
        public List<Cooperative> findAllById(Iterable<Long> longs) {
            return List.of();
        }

        @Override
        public long count() {
            return 0;
        }

        @Override
        public void deleteById(Long aLong) {
        }

        @Override
        public void delete(Cooperative entity) {
        }

        @Override
        public void deleteAllById(Iterable<? extends Long> longs) {
        }

        @Override
        public void deleteAll(Iterable<? extends Cooperative> entities) {
        }

        @Override
        public void deleteAll() {
        }

        @Override
        public <S extends Cooperative> List<S> saveAll(Iterable<S> entities) {
            return List.of();
        }

        @Override
        public void flush() {
        }

        @Override
        public <S extends Cooperative> S saveAndFlush(S entity) {
            return null;
        }

        @Override
        public <S extends Cooperative> List<S> saveAllAndFlush(Iterable<S> entities) {
            return List.of();
        }

        @Override
        public void deleteAllInBatch(Iterable<Cooperative> entities) {
        }

        @Override
        public void deleteAllByIdInBatch(Iterable<Long> longs) {
        }

        @Override
        public void deleteAllInBatch() {
        }

        @Override
        public Cooperative getOne(Long aLong) {
            return null;
        }

        @Override
        public Cooperative getById(Long aLong) {
            return null;
        }

        @Override
        public Cooperative getReferenceById(Long aLong) {
            return null;
        }

        @Override
        public <S extends Cooperative> Optional<S> findOne(Example<S> example) {
            return Optional.empty();
        }

        @Override
        public <S extends Cooperative> List<S> findAll(Example<S> example) {
            return List.of();
        }

        @Override
        public <S extends Cooperative> List<S> findAll(Example<S> example, Sort sort) {
            return List.of();
        }

        @Override
        public <S extends Cooperative> Page<S> findAll(Example<S> example, Pageable pageable) {
            return Page.empty();
        }

        @Override
        public <S extends Cooperative> long count(Example<S> example) {
            return 0;
        }

        @Override
        public <S extends Cooperative> boolean exists(Example<S> example) {
            return false;
        }

        @Override
        public <S extends Cooperative, R> R findBy(Example<S> example,
                Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
            return null;
        }

        @Override
        public List<Cooperative> findAll(Sort sort) {
            return List.of();
        }

        @Override
        public Page<Cooperative> findAll(Pageable pageable) {
            return Page.empty();
        }
    }
}
