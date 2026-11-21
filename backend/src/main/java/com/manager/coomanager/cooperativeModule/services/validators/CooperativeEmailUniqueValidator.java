package com.manager.coomanager.cooperativeModule.services.validators;

import com.manager.coomanager.cooperativeModule.dto.CooperativeRequestDto;
import com.manager.coomanager.cooperativeModule.repositories.CooperativeRepository;
import com.manager.coomanager.shared.domain.exceptions.DomainException;
import com.manager.coomanager.shared.domain.valueobjects.Email;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Validador que assegura a unicidade do e-mail institucional entre cooperativas cadastradas.
 */
@Component
public class CooperativeEmailUniqueValidator implements CooperativeValidator {

    private final CooperativeRepository repository;

    public CooperativeEmailUniqueValidator(CooperativeRepository repository) {
        this.repository = repository;
    }

    @Override
    public void validate(CooperativeRequestDto dto, Long targetId) {
        if (dto.email() == null || dto.email().isBlank()) {
            return;
        }

        Email email = new Email(dto.email());
        repository.findByEmail(email).ifPresent(existing -> {
            if (!Objects.equals(existing.getId(), targetId)) {
                throw new DomainException("Já existe uma cooperativa cadastrada com o e-mail informado.");
            }
        });
    }
}

