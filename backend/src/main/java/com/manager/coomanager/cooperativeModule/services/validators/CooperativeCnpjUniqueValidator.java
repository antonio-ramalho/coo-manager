package com.manager.coomanager.cooperativeModule.services.validators;

import com.manager.coomanager.cooperativeModule.dto.CooperativeRequestDto;
import com.manager.coomanager.cooperativeModule.repositories.CooperativeRepository;
import com.manager.coomanager.shared.domain.exceptions.DomainException;
import com.manager.coomanager.shared.domain.valueobjects.Cnpj;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Validador que assegura a unicidade do CNPJ entre cooperativas cadastradas.
 */
@Component
public class CooperativeCnpjUniqueValidator implements CooperativeValidator {

    private final CooperativeRepository repository;

    public CooperativeCnpjUniqueValidator(CooperativeRepository repository) {
        this.repository = repository;
    }

    @Override
    public void validate(CooperativeRequestDto dto, Long targetId) {
        if (dto.cnpj() == null || dto.cnpj().isBlank()) {
            return;
        }

        Cnpj cnpj = new Cnpj(dto.cnpj());
        repository.findByCnpj(cnpj).ifPresent(existing -> {
            if (!Objects.equals(existing.getId(), targetId)) {
                throw new DomainException("Já existe uma cooperativa cadastrada com o CNPJ informado.");
            }
        });
    }
}

