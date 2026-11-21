package com.manager.coomanager.iamModule.services.validators;

import com.manager.coomanager.shared.domain.exceptions.DomainException;
import com.manager.coomanager.shared.domain.valueobjects.Email;
import com.manager.coomanager.iamModule.dto.UserRequestDto;
import com.manager.coomanager.iamModule.repositories.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Validador que assegura a unicidade do e-mail do usuário no sistema.
 */
@Component
public class UserEmailUniqueValidator implements UserValidator {

    private final UserRepository userRepository;

    public UserEmailUniqueValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void validate(UserRequestDto dto, Long targetId) {
        if (dto.email() == null || dto.email().isBlank()) {
            return;
        }

        Email email = new Email(dto.email());
        userRepository.findByEmail(email).ifPresent(existing -> {
            if (!Objects.equals(existing.getId(), targetId)) {
                throw new DomainException("Já existe um usuário cadastrado com o e-mail informado.");
            }
        });
    }
}

