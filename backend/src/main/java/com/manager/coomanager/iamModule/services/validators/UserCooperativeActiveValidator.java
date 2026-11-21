package com.manager.coomanager.iamModule.services.validators;

import com.manager.coomanager.cooperativeModule.entities.Cooperative;
import com.manager.coomanager.cooperativeModule.repositories.CooperativeRepository;
import com.manager.coomanager.shared.domain.exceptions.DomainException;
import com.manager.coomanager.shared.domain.exceptions.ResourceNotFoundException;
import com.manager.coomanager.personModule.enums.Status;
import com.manager.coomanager.iamModule.dto.UserRequestDto;
import org.springframework.stereotype.Component;

/**
 * Validador que verifica se a cooperativa informada existe e está ativa para vincular novos usuários.
 */
@Component
public class UserCooperativeActiveValidator implements UserValidator {

    private final CooperativeRepository cooperativeRepository;

    public UserCooperativeActiveValidator(CooperativeRepository cooperativeRepository) {
        this.cooperativeRepository = cooperativeRepository;
    }

    @Override
    public void validate(UserRequestDto dto, Long targetId) {
        if (dto.cooperativeId() == null) {
            throw new DomainException("A cooperativa é obrigatória para o cadastro do usuário.");
        }

        Cooperative cooperative = cooperativeRepository.findById(dto.cooperativeId())
                .orElseThrow(() -> new ResourceNotFoundException("Cooperativa não encontrada com o ID: " + dto.cooperativeId()));

        if (cooperative.getStatus() != Status.ACTIVE) {
            throw new DomainException("Não é possível cadastrar usuários em uma cooperativa inativa ou suspensa.");
        }
    }
}

