package com.manager.coomanager.userModule.services.validators;

import com.manager.coomanager.userModule.dto.RoleRequestDto;

/**
 * Interface Strategy para validação de regras de negócio de Cargos (Roles).
 */
public interface RoleValidator {

    /**
     * Executa a regra de validação sobre o DTO de cargo.
     *
     * @param dto dados do cargo
     * @param targetId identificador do cargo em atualização ou null em cadastro
     */
    void validate(RoleRequestDto dto, Long targetId);
}

