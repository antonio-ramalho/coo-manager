package com.manager.coomanager.cooperativeModule.services;

import com.manager.coomanager.cooperativeModule.dto.CooperativeMinDto;
import com.manager.coomanager.cooperativeModule.dto.CooperativeRequestDto;
import com.manager.coomanager.cooperativeModule.dto.CooperativeResponseDto;
import com.manager.coomanager.cooperativeModule.entities.Cooperative;
import com.manager.coomanager.cooperativeModule.repositories.CooperativeRepository;
import com.manager.coomanager.cooperativeModule.services.validators.CooperativeValidator;
import com.manager.coomanager.shared.domain.exceptions.ResourceNotFoundException;
import com.manager.coomanager.shared.domain.valueobjects.*;
import com.manager.coomanager.shared.domain.valueobjects.dto.AddressDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CooperativeService {

    private final CooperativeRepository repository;
    private final List<CooperativeValidator> validators;

    public CooperativeService(CooperativeRepository repository, List<CooperativeValidator> validators) {
        this.repository = repository;
        this.validators = validators;
    }

    @Transactional(readOnly = true)
    public Page<CooperativeMinDto> findAllPaged(Pageable pageable, String searchTerm) {
        String term = (searchTerm != null) ? searchTerm.trim() : "";
        return repository.findAllBySearchTerm(term, pageable).map(CooperativeMinDto::new);
    }

    @Transactional(readOnly = true)
    public CooperativeResponseDto findById(Long id) {
        Cooperative cooperative = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cooperativa não encontrada com o ID: " + id));
        return new CooperativeResponseDto(cooperative);
    }

    @Transactional
    public CooperativeResponseDto insert(CooperativeRequestDto dto) {
        runValidators(dto, null);

        Cooperative entity = buildEntityFromDto(dto);
        Cooperative saved = repository.save(entity);

        return new CooperativeResponseDto(saved);
    }

    @Transactional
    public CooperativeResponseDto update(Long id, CooperativeRequestDto dto) {
        Cooperative cooperative = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cooperativa não encontrada com o ID: " + id));

        runValidators(dto, id);
        updateData(cooperative, dto);

        Cooperative updated = repository.save(cooperative);
        return new CooperativeResponseDto(updated);
    }

    @Transactional
    public void deactivate(Long id) {
        Cooperative cooperative = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cooperativa não encontrada com o ID: " + id));
        cooperative.deactivate();
        repository.save(cooperative);
    }

    @Transactional
    public void activate(Long id) {
        Cooperative cooperative = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cooperativa não encontrada com o ID: " + id));
        cooperative.activate();
        repository.save(cooperative);
    }

    private void runValidators(CooperativeRequestDto dto, Long targetId) {
        for (CooperativeValidator validator : validators) {
            validator.validate(dto, targetId);
        }
    }

    private Cooperative buildEntityFromDto(CooperativeRequestDto dto) {
        Address address = createAddressFromDto(dto.address());
        Email email = new Email(dto.email());
        Phone phone = new Phone(dto.phone());
        Cnpj cnpj = new Cnpj(dto.cnpj());
        StateRegistration stateRegistration = new StateRegistration(dto.stateRegistration());

        return new Cooperative(
                address,
                email,
                phone,
                dto.foundationDate(),
                cnpj,
                dto.legalName(),
                dto.tradeName(),
                stateRegistration);
    }

    private void updateData(Cooperative entity, CooperativeRequestDto dto) {
        entity.updateName(dto.legalName());
        entity.updateTradeName(dto.tradeName());
        entity.updateEmail(new Email(dto.email()));
        entity.updatePhone(new Phone(dto.phone()));
        entity.updateFoundationDate(dto.foundationDate());
        entity.updateStateRegistration(new StateRegistration(dto.stateRegistration()));

        if (dto.address() != null) {
            entity.updateAddress(createAddressFromDto(dto.address()));
        }
    }

    private Address createAddressFromDto(AddressDto dto) {
        if (dto == null) {
            return null;
        }
        return new Address(
                new Cep(dto.zipCode()),
                dto.street(),
                dto.neighborhood(),
                dto.city(),
                dto.number(),
                dto.state());
    }
}
