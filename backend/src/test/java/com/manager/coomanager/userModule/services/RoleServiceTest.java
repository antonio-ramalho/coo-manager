package com.manager.coomanager.userModule.services;

import com.manager.coomanager.cooperativeModule.entities.Cooperative;
import com.manager.coomanager.cooperativeModule.repositories.CooperativeRepository;
import com.manager.coomanager.infrastructure.exceptions.DomainException;
import com.manager.coomanager.infrastructure.exceptions.ResourceNotFoundException;
import com.manager.coomanager.infrastructure.valueObjects.*;
import com.manager.coomanager.personModule.enums.BrazilianState;
import com.manager.coomanager.userModule.dto.RoleRequestDto;
import com.manager.coomanager.userModule.dto.RoleResponseDto;
import com.manager.coomanager.userModule.entities.Role;
import com.manager.coomanager.userModule.enums.Permission;
import com.manager.coomanager.userModule.repositories.RoleRepository;
import com.manager.coomanager.userModule.services.validators.RoleNameUniqueValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RoleServiceTest {

    private InMemoryRoleRepository roleRepository;
    private InMemoryCooperativeRepository cooperativeRepository;
    private RoleService service;
    private Cooperative sampleCooperative;

    @BeforeEach
    void setUp() {
        roleRepository = new InMemoryRoleRepository();
        cooperativeRepository = new InMemoryCooperativeRepository();
        RoleNameUniqueValidator validator = new RoleNameUniqueValidator(roleRepository);

        service = new RoleService(roleRepository, cooperativeRepository, List.of(validator));

        sampleCooperative = new Cooperative(
                new Address(new Cep("56300-000"), "Rua das Flores", "Centro", "Petrolina", "10", BrazilianState.PE),
                new Email("coop@teste.com"),
                new Phone("87999998888"),
                LocalDate.of(2021, 1, 1),
                new Cnpj("12.345.678/0001-95"),
                "Coop Teste LTDA",
                "Coop Teste",
                new StateRegistration("123456789")
        );
        ReflectionTestUtils.setField(sampleCooperative, "id", 1L);
        cooperativeRepository.save(sampleCooperative);
    }

    @Test
    @DisplayName("Deve cadastrar um novo cargo com permissões específicas")
    void shouldCreateRoleSuccessfully() {
        RoleRequestDto dto = new RoleRequestDto(
                "Assistente de Editais",
                "Responsável por cadastrar e acompanhar editais",
                1L,
                Set.of(Permission.CONTRACT_READ, Permission.CONTRACT_WRITE)
        );

        RoleResponseDto response = service.insert(dto);

        assertThat(response).isNotNull();
        assertThat(response.id()).isNotNull();
        assertThat(response.name()).isEqualTo("Assistente de Editais");
        assertThat(response.permissions()).containsExactlyInAnyOrder(Permission.CONTRACT_READ, Permission.CONTRACT_WRITE);
    }

    @Test
    @DisplayName("Deve impedir criação de cargo com nome duplicado na mesma cooperativa")
    void shouldPreventDuplicateRoleNameInSameCooperative() {
        RoleRequestDto dto1 = new RoleRequestDto("Gerente Geral", "Desc", 1L, Set.of(Permission.USER_MANAGE));
        service.insert(dto1);

        RoleRequestDto dto2 = new RoleRequestDto("Gerente Geral", "Outra desc", 1L, Set.of(Permission.CLIENT_READ));
        assertThatThrownBy(() -> service.insert(dto2))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Já existe um cargo cadastrado com este nome");
    }

    @Test
    @DisplayName("Deve impedir alteração ou exclusão de cargos do sistema")
    void shouldProtectSystemRoles() {
        Role systemRole = new Role("SUPER_ADMIN", "Cargo do Sistema", null, true);
        ReflectionTestUtils.setField(systemRole, "id", 999L);
        roleRepository.save(systemRole);

        RoleRequestDto updateDto = new RoleRequestDto("SUPER_ADMIN_EDITADO", "Desc", null, Set.of());
        assertThatThrownBy(() -> service.update(999L, updateDto))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Cargos padrão do sistema não podem ser alterados");

        assertThatThrownBy(() -> service.delete(999L))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Cargos padrão do sistema não podem ser excluídos");
    }

    private static class InMemoryRoleRepository implements RoleRepository {
        private final Map<Long, Role> database = new HashMap<>();
        private long seq = 1L;

        @Override
        public List<Role> findByCooperativeId(Long cooperativeId) {
            return database.values().stream()
                    .filter(r -> r.getCooperative() != null && Objects.equals(r.getCooperative().getId(), cooperativeId))
                    .toList();
        }

        @Override
        public List<Role> findBySystemRoleTrue() {
            return database.values().stream().filter(Role::isSystemRole).toList();
        }

        @Override
        public List<Role> findAllAvailableForCooperative(Long cooperativeId) {
            return database.values().stream()
                    .filter(r -> r.isSystemRole() || (r.getCooperative() != null && Objects.equals(r.getCooperative().getId(), cooperativeId)))
                    .toList();
        }

        @Override
        public boolean existsByNameAndCooperativeId(String name, Long cooperativeId) {
            return findByNameAndCooperativeId(name, cooperativeId).isPresent();
        }

        @Override
        public Optional<Role> findByNameAndCooperativeId(String name, Long cooperativeId) {
            return database.values().stream()
                    .filter(r -> r.getName().equalsIgnoreCase(name) &&
                            r.getCooperative() != null &&
                            Objects.equals(r.getCooperative().getId(), cooperativeId))
                    .findFirst();
        }

        @Override
        public <S extends Role> S save(S entity) {
            if (entity.getId() == null) {
                ReflectionTestUtils.setField(entity, "id", seq++);
            }
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
            return new ArrayList<>(database.values());
        }

        @Override
        public List<Role> findAllById(Iterable<Long> longs) {
            List<Role> list = new ArrayList<>();
            for (Long id : longs) {
                if (database.containsKey(id)) list.add(database.get(id));
            }
            return list;
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
        public void delete(Role entity) {
            database.remove(entity.getId());
        }

        @Override public void deleteAllById(Iterable<? extends Long> longs) {}
        @Override public void deleteAll(Iterable<? extends Role> entities) {}
        @Override public void deleteAll() {}
        @Override public <S extends Role> List<S> saveAll(Iterable<S> entities) { return List.of(); }
        @Override public void flush() {}
        @Override public <S extends Role> S saveAndFlush(S entity) { return save(entity); }
        @Override public <S extends Role> List<S> saveAllAndFlush(Iterable<S> entities) { return List.of(); }
        @Override public void deleteAllInBatch(Iterable<Role> entities) {}
        @Override public void deleteAllByIdInBatch(Iterable<Long> longs) {}
        @Override public void deleteAllInBatch() {}
        @Override public Role getOne(Long aLong) { return database.get(aLong); }
        @Override public Role getById(Long aLong) { return database.get(aLong); }
        @Override public Role getReferenceById(Long aLong) { return database.get(aLong); }
        @Override public <S extends Role> Optional<S> findOne(Example<S> example) { return Optional.empty(); }
        @Override public <S extends Role> List<S> findAll(Example<S> example) { return List.of(); }
        @Override public <S extends Role> List<S> findAll(Example<S> example, Sort sort) { return List.of(); }
        @Override public <S extends Role> Page<S> findAll(Example<S> example, Pageable pageable) { return Page.empty(); }
        @Override public <S extends Role> long count(Example<S> example) { return 0; }
        @Override public <S extends Role> boolean exists(Example<S> example) { return false; }
        @Override public <S extends Role, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) { return null; }
        @Override public List<Role> findAll(Sort sort) { return List.of(); }
        @Override public Page<Role> findAll(Pageable pageable) { return Page.empty(); }
    }

    private static class InMemoryCooperativeRepository implements CooperativeRepository {
        private final Map<Long, Cooperative> database = new HashMap<>();
        @Override public boolean existsByCnpj(Cnpj cnpj) { return false; }
        @Override public boolean existsByEmail(Email email) { return false; }
        @Override public Optional<Cooperative> findByCnpj(Cnpj cnpj) { return Optional.empty(); }
        @Override public Optional<Cooperative> findByEmail(Email email) { return Optional.empty(); }
        @Override public Page<Cooperative> findAllBySearchTerm(String searchTerm, Pageable pageable) { return Page.empty(); }
        @Override public <S extends Cooperative> S save(S entity) {
            database.put(entity.getId(), entity);
            return entity;
        }
        @Override public Optional<Cooperative> findById(Long aLong) { return Optional.ofNullable(database.get(aLong)); }
        @Override public boolean existsById(Long aLong) { return database.containsKey(aLong); }
        @Override public List<Cooperative> findAll() { return List.of(); }
        @Override public List<Cooperative> findAllById(Iterable<Long> longs) { return List.of(); }
        @Override public long count() { return 0; }
        @Override public void deleteById(Long aLong) {}
        @Override public void delete(Cooperative entity) {}
        @Override public void deleteAllById(Iterable<? extends Long> longs) {}
        @Override public void deleteAll(Iterable<? extends Cooperative> entities) {}
        @Override public void deleteAll() {}
        @Override public <S extends Cooperative> List<S> saveAll(Iterable<S> entities) { return List.of(); }
        @Override public void flush() {}
        @Override public <S extends Cooperative> S saveAndFlush(S entity) { return null; }
        @Override public <S extends Cooperative> List<S> saveAllAndFlush(Iterable<S> entities) { return List.of(); }
        @Override public void deleteAllInBatch(Iterable<Cooperative> entities) {}
        @Override public void deleteAllByIdInBatch(Iterable<Long> longs) {}
        @Override public void deleteAllInBatch() {}
        @Override public Cooperative getOne(Long aLong) { return null; }
        @Override public Cooperative getById(Long aLong) { return null; }
        @Override public Cooperative getReferenceById(Long aLong) { return null; }
        @Override public <S extends Cooperative> Optional<S> findOne(Example<S> example) { return Optional.empty(); }
        @Override public <S extends Cooperative> List<S> findAll(Example<S> example) { return List.of(); }
        @Override public <S extends Cooperative> List<S> findAll(Example<S> example, Sort sort) { return List.of(); }
        @Override public <S extends Cooperative> Page<S> findAll(Example<S> example, Pageable pageable) { return Page.empty(); }
        @Override public <S extends Cooperative> long count(Example<S> example) { return 0; }
        @Override public <S extends Cooperative> boolean exists(Example<S> example) { return false; }
        @Override public <S extends Cooperative, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) { return null; }
        @Override public List<Cooperative> findAll(Sort sort) { return List.of(); }
        @Override public Page<Cooperative> findAll(Pageable pageable) { return Page.empty(); }
    }
}

