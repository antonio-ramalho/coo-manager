package com.manager.coomanager.cooperativeModule.services.validators;

import com.manager.coomanager.cooperativeModule.dto.CooperativeRequestDto;

/**
 * Interface Strategy para regras de validação de Cooperativa.
 */
public interface CooperativeValidator {

    /**
     * Valida os dados da cooperativa.
     *
     * @param dto dados submetidos
     * @param targetId identificador da cooperativa em caso de atualização (ou null para inserção)
     */
    void validate(CooperativeRequestDto dto, Long targetId);
}

