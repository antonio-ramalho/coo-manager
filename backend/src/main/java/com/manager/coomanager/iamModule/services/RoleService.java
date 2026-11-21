package com.manager.coomanager.iamModule.services;

import com.manager.coomanager.cooperativeModule.entities.Cooperative;
import com.manager.coomanager.cooperativeModule.repositories.CooperativeRepository;
import com.manager.coomanager.shared.domain.exceptions.DomainException;
import com.manager.coomanager.shared.domain.exceptions.ResourceNotFoundException;
import com.manager.coomanager.iamModule.dto.RoleRequestDto;
import com.manager.coomanager.iamModule.dto.RoleResponseDto;
import com.manager.coomanager.iamModule.entities.Role;
import com.manager.coomanager.iamModule.repositories.RoleRepository;
import com.manager.coomanager.iamModule.services.validators.RoleValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final CooperativeRepository cooperativeRepository;
    private final List<RoleValidator> validators;

    public RoleService(RoleRepository roleRepository,
            CooperativeRepository cooperativeRepository,
            List<RoleValidator> validators) {
        this.roleRepository = roleRepository;
        this.cooperativeRepository = cooperativeRepository;
        this.validators = validators;
    }

    @Transactional(readOnly = true)
    public List<RoleResponseDto> findAllForCooperative(Long cooperativeId) {
        return roleRepository.findAllAvailableForCooperative(cooperativeId)
                .stream()
                .map(RoleResponseDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public RoleResponseDto findById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cargo não encontrado com o ID: " + id));
        return new RoleResponseDto(role);
    }

    @Transactional
    public RoleResponseDto insert(RoleRequestDto dto) {
        runValidators(dto, null);

        Cooperative cooperative = null;
        if (dto.cooperativeId() != null) {
            cooperative = cooperativeRepository.findById(dto.cooperativeId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Cooperativa não encontrada com o ID: " + dto.cooperativeId()));
        }

        Role role = new Role(dto.name().trim(), dto.description(), cooperative, false);
        role.setPermissions(dto.permissions());

        Role saved = roleRepository.save(role);
        return new RoleResponseDto(saved);
    }

    @Transactional
    public RoleResponseDto update(Long id, RoleRequestDto dto) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cargo não encontrado com o ID: " + id));

        if (role.isSystemRole()) {
            throw new DomainException("Cargos padrão do sistema não podem ser alterados.");
        }

        runValidators(dto, id);

        role.updateName(dto.name().trim());
        role.updateDescription(dto.description());
        role.setPermissions(dto.permissions());

        Role updated = roleRepository.save(role);
        return new RoleResponseDto(updated);
    }

    @Transactional
    public void delete(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cargo não encontrado com o ID: " + id));

        if (role.isSystemRole()) {
            throw new DomainException("Cargos padrão do sistema não podem ser excluídos.");
        }

        roleRepository.delete(role);
    }

    private void runValidators(RoleRequestDto dto, Long targetId) {
        for (RoleValidator validator : validators) {
            validator.validate(dto, targetId);
        }
    }
}
