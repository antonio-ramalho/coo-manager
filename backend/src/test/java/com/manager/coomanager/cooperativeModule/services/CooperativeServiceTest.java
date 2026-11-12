package com.manager.coomanager.cooperativeModule.services;

import com.manager.coomanager.cooperativeModule.dto.CooperativeMinDto;
import com.manager.coomanager.cooperativeModule.dto.CooperativeRequestDto;
import com.manager.coomanager.cooperativeModule.dto.CooperativeResponseDto;
import com.manager.coomanager.cooperativeModule.entities.Cooperative;
import com.manager.coomanager.cooperativeModule.repositories.CooperativeRepository;
import com.manager.coomanager.cooperativeModule.services.validators.CooperativeCnpjUniqueValidator;
import com.manager.coomanager.cooperativeModule.services.validators.CooperativeEmailUniqueValidator;
import com.manager.coomanager.cooperativeModule.services.validators.CooperativeValidator;
import com.manager.coomanager.infrastructure.exceptions.DomainException;
import com.manager.coomanager.infrastructure.exceptions.ResourceNotFoundException;
import com.manager.coomanager.infrastructure.valueObjects.*;
import com.manager.coomanager.infrastructure.valueObjectsDto.AddressDto;
import com.manager.coomanager.personModule.enums.BrazilianState;
import com.manager.coomanager.personModule.enums.Status;
import org.junit.jupiter.api.BeforeEach;
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

class CooperativeServiceTest {

    private InMemoryCooperativeRepository repository;
    private CooperativeService service;
    private CooperativeRequestDto sampleRequestDto;

    @BeforeEach
    void setUp() {
        repository = new InMemoryCooperativeRepository();
        CooperativeCnpjUniqueValidator cnpjValidator = new CooperativeCnpjUniqueValidator(repository);
        CooperativeEmailUniqueValidator emailValidator = new CooperativeEmailUniqueValidator(repository);
        List<CooperativeValidator> validators = List.of(cnpjValidator, emailValidator);

        service = new CooperativeService(repository, validators);

        AddressDto addressDto = new AddressDto(
                "Rua das Palmeiras",
                "100",
                "Centro",
                "Petrolina",
                "56300-000",
                BrazilianState.PE
        );

        sampleRequestDto = new CooperativeRequestDto(
                "Cooperativa Agricola Vale do Sol LTDA",
                "Coop Vale do Sol",
                "12.345.678/0001-95",
                "1234567890",
                "contato@valedosol.coop.br",
                "87988887777",
                LocalDate.of(2015, 6, 10),
                addressDto
        );
    }

    @Test
    @DisplayName("Deve cadastrar uma nova cooperativa com sucesso e registrar data de criação")
    void shouldInsertCooperativeSuccessfully() {
        CooperativeResponseDto response = service.insert(sampleRequestDto);

        assertThat(response).isNotNull();
        assertThat(response.id()).isNotNull();
        assertThat(response.legalName()).isEqualTo("Cooperativa Agricola Vale do Sol LTDA");
        assertThat(response.cnpj()).isEqualTo("12345678000195");
        assertThat(response.stateRegistration()).isEqualTo("1234567890");
        assertThat(response.registrationDate()).isNotNull();

        Cooperative saved = repository.findById(response.id()).orElseThrow();
        assertThat(saved.getRegistrationDate()).isNotNull();
    }

    @Test
    @DisplayName("Deve lançar DomainException quando tentar cadastrar cooperativa com CNPJ duplicado")
    void shouldThrowExceptionWhenCnpjAlreadyExists() {
        service.insert(sampleRequestDto);

        CooperativeRequestDto duplicateCnpjDto = new CooperativeRequestDto(
                "Outra Cooperativa",
                "Outra Coop",
                "12.345.678/0001-95",
                "9876543210",
                "outro@valedosol.coop.br",
                "87999990000",
                LocalDate.of(2018, 1, 1),
                sampleRequestDto.address()
        );

        assertThatThrownBy(() -> service.insert(duplicateCnpjDto))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Já existe uma cooperativa cadastrada com o CNPJ informado");
    }

    @Test
    @DisplayName("Deve lançar DomainException quando tentar cadastrar cooperativa com e-mail duplicado")
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        service.insert(sampleRequestDto);

        CooperativeRequestDto duplicateEmailDto = new CooperativeRequestDto(
                "Outra Cooperativa",
                "Outra Coop",
                "99.888.777/0001-66",
                "9876543210",
                "contato@valedosol.coop.br",
                "87999990000",
                LocalDate.of(2018, 1, 1),
                sampleRequestDto.address()
        );

        assertThatThrownBy(() -> service.insert(duplicateEmailDto))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Já existe uma cooperativa cadastrada com o e-mail informado");
    }

    @Test
    @DisplayName("Deve buscar cooperativa por ID existente com sucesso")
    void shouldFindByIdSuccessfully() {
        CooperativeResponseDto created = service.insert(sampleRequestDto);

        CooperativeResponseDto response = service.findById(created.id());

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(created.id());
        assertThat(response.legalName()).isEqualTo("Cooperativa Agricola Vale do Sol LTDA");
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao buscar ID inexistente")
    void shouldThrowExceptionWhenNotFoundById() {
        assertThatThrownBy(() -> service.findById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Cooperativa não encontrada com o ID: 999");
    }

    @Test
    @DisplayName("Deve inativar e reativar cooperativa com sucesso")
    void shouldDeactivateAndActivateCooperative() {
        CooperativeResponseDto created = service.insert(sampleRequestDto);

        service.deactivate(created.id());
        Cooperative deactivated = repository.findById(created.id()).orElseThrow();
        assertThat(deactivated.getStatus()).isEqualTo(Status.INACTIVE);

        service.activate(created.id());
        Cooperative activated = repository.findById(created.id()).orElseThrow();
        assertThat(activated.getStatus()).isEqualTo(Status.ACTIVE);
    }

    @Test
    @DisplayName("Deve listar cooperativas de forma paginada")
    void shouldFindAllPaged() {
        service.insert(sampleRequestDto);

        Page<CooperativeMinDto> page = service.findAllPaged(PageRequest.of(0, 10), "Vale do Sol");
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent().get(0).tradeName()).isEqualTo("Coop Vale do Sol");
    }

    /**
     * Fake repository em memória para testes unitários de alta velocidade sem dependência
     * de Dynamic Agent Attach do Java 25.
     */
    private static class InMemoryCooperativeRepository implements CooperativeRepository {
        private final Map<Long, Cooperative> database = new HashMap<>();
        private long sequence = 1L;

        @Override
        public boolean existsByCnpj(Cnpj cnpj) {
            return database.values().stream()
                    .anyMatch(c -> c.getCnpj() != null && c.getCnpj().equals(cnpj));
        }

        @Override
        public boolean existsByEmail(Email email) {
            return database.values().stream()
                    .anyMatch(c -> c.getEmail() != null && c.getEmail().equals(email));
        }

        @Override
        public Optional<Cooperative> findByCnpj(Cnpj cnpj) {
            return database.values().stream()
                    .filter(c -> c.getCnpj() != null && c.getCnpj().equals(cnpj))
                    .findFirst();
        }

        @Override
        public Optional<Cooperative> findByEmail(Email email) {
            return database.values().stream()
                    .filter(c -> c.getEmail() != null && c.getEmail().equals(email))
                    .findFirst();
        }

        @Override
        public Page<Cooperative> findAllBySearchTerm(String searchTerm, Pageable pageable) {
            List<Cooperative> list = database.values().stream()
                    .filter(c -> searchTerm.isBlank() ||
                            c.getLegalName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                            c.getTradeName().toLowerCase().contains(searchTerm.toLowerCase()))
                    .toList();
            return new PageImpl<>(list, pageable, list.size());
        }

        @Override
        public <S extends Cooperative> S save(S entity) {
            if (entity.getId() == null) {
                ReflectionTestUtils.setField(entity, "id", sequence++);
            }
            database.put(entity.getId(), entity);
            return entity;
        }

        @Override
        public Optional<Cooperative> findById(Long id) {
            return Optional.ofNullable(database.get(id));
        }

        @Override
        public boolean existsById(Long id) {
            return database.containsKey(id);
        }

        @Override
        public List<Cooperative> findAll() {
            return new ArrayList<>(database.values());
        }

        @Override
        public List<Cooperative> findAllById(Iterable<Long> longs) {
            return List.of();
        }

        @Override
        public long count() {
            return database.size();
        }

        @Override
        public void deleteById(Long id) {
            database.remove(id);
        }

        @Override
        public void delete(Cooperative entity) {
            database.remove(entity.getId());
        }

        @Override
        public void deleteAllById(Iterable<? extends Long> longs) {}

        @Override
        public void deleteAll(Iterable<? extends Cooperative> entities) {}

        @Override
        public void deleteAll() {
            database.clear();
        }

        @Override
        public <S extends Cooperative> List<S> saveAll(Iterable<S> entities) {
            return List.of();
        }

        @Override
        public void flush() {}

        @Override
        public <S extends Cooperative> S saveAndFlush(S entity) {
            return save(entity);
        }

        @Override
        public <S extends Cooperative> List<S> saveAllAndFlush(Iterable<S> entities) {
            return List.of();
        }

        @Override
        public void deleteAllInBatch(Iterable<Cooperative> entities) {}

        @Override
        public void deleteAllByIdInBatch(Iterable<Long> longs) {}

        @Override
        public void deleteAllInBatch() {}

        @Override
        public Cooperative getOne(Long aLong) {
            return database.get(aLong);
        }

        @Override
        public Cooperative getById(Long aLong) {
            return database.get(aLong);
        }

        @Override
        public Cooperative getReferenceById(Long aLong) {
            return database.get(aLong);
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
        public <S extends Cooperative, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
            return null;
        }

        @Override
        public List<Cooperative> findAll(Sort sort) {
            return List.of();
        }

        @Override
        public Page<Cooperative> findAll(Pageable pageable) {
            return new PageImpl<>(new ArrayList<>(database.values()), pageable, database.size());
        }
    }
}
