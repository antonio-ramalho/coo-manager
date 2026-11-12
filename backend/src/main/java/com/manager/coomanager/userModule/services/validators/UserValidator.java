package com.manager.coomanager.userModule.services.validators;

import com.manager.coomanager.userModule.dto.UserRequestDto;

/**
 * Interface Strategy para validação de regras de negócio de Usuários.
 */
public interface UserValidator {

    /**
     * Executa a regra de validação sobre o DTO de usuário.
     *
     * @param dto dados da requisição
     * @param targetId identificador do usuário em atualização ou null em cadastro
     */
    void validate(UserRequestDto dto, Long targetId);
}

