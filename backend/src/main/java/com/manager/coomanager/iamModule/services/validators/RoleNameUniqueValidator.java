package com.manager.coomanager.iamModule.services.validators;

import com.manager.coomanager.shared.domain.exceptions.DomainException;
import com.manager.coomanager.iamModule.dto.RoleRequestDto;
import com.manager.coomanager.iamModule.repositories.RoleRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Validador que assegura a unicidade do nome do cargo dentro da mesma cooperativa.
 */
@Component
public class RoleNameUniqueValidator implements RoleValidator {

    private final RoleRepository roleRepository;

    public RoleNameUniqueValidator(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void validate(RoleRequestDto dto, Long targetId) {
        if (dto.name() == null || dto.name().isBlank()) {
            throw new DomainException("O nome do cargo não pode ser vazio.");
        }

        if (dto.cooperativeId() != null) {
            roleRepository.findByNameAndCooperativeId(dto.name().trim(), dto.cooperativeId())
                    .ifPresent(existing -> {
                        if (!Objects.equals(existing.getId(), targetId)) {
                            throw new DomainException("Já existe um cargo cadastrado com este nome nesta cooperativa.");
                        }
                    });
        }
    }
}

