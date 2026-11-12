package com.manager.coomanager.userModule.services;

import com.manager.coomanager.cooperativeModule.entities.Cooperative;
import com.manager.coomanager.cooperativeModule.repositories.CooperativeRepository;
import com.manager.coomanager.infrastructure.exceptions.ResourceNotFoundException;
import com.manager.coomanager.infrastructure.valueObjects.Email;
import com.manager.coomanager.infrastructure.valueObjects.PasswordHash;
import com.manager.coomanager.userModule.dto.UserMinDto;
import com.manager.coomanager.userModule.dto.UserRequestDto;
import com.manager.coomanager.userModule.dto.UserResponseDto;
import com.manager.coomanager.userModule.dto.UserUpdateDto;
import com.manager.coomanager.userModule.entities.Role;
import com.manager.coomanager.userModule.entities.User;
import com.manager.coomanager.userModule.repositories.RoleRepository;
import com.manager.coomanager.userModule.repositories.UserRepository;
import com.manager.coomanager.userModule.services.validators.UserValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CooperativeRepository cooperativeRepository;
    private final List<UserValidator> validators;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
            RoleRepository roleRepository,
            CooperativeRepository cooperativeRepository,
            List<UserValidator> validators,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.cooperativeRepository = cooperativeRepository;
        this.validators = validators;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public Page<UserMinDto> findAllByCooperative(Long cooperativeId, Pageable pageable, String searchTerm) {
        String term = searchTerm != null ? searchTerm.trim() : "";
        return userRepository.searchByCooperativeAndTerm(cooperativeId, term, pageable)
                .map(UserMinDto::new);
    }

    @Transactional(readOnly = true)
    public UserResponseDto findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + id));
        return new UserResponseDto(user);
    }

    @Transactional
    public UserResponseDto insert(UserRequestDto dto) {
        runValidators(dto, null);

        Cooperative cooperative = cooperativeRepository.findById(dto.cooperativeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cooperativa não encontrada com o ID: " + dto.cooperativeId()));

        PasswordHash passwordHash = (dto.password() != null && !dto.password().isBlank())
                ? new PasswordHash(passwordEncoder.encode(dto.password()))
                : null;

        User user = new User(dto.fullName().trim(), new Email(dto.email()), passwordHash, cooperative);

        if (dto.roleIds() != null && !dto.roleIds().isEmpty()) {
            List<Role> roles = roleRepository.findAllById(dto.roleIds());
            user.setRoles(new HashSet<>(roles));
        }

        User saved = userRepository.save(user);
        return new UserResponseDto(saved);
    }

    @Transactional
    public UserResponseDto update(Long id, UserUpdateDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + id));

        if (dto.email() != null && !dto.email().isBlank()) {
            UserRequestDto mockRequest = new UserRequestDto(
                    dto.fullName(),
                    dto.email(),
                    null,
                    user.getCooperative().getId(),
                    dto.roleIds());
            runValidators(mockRequest, id);
            user.updateEmail(new Email(dto.email()));
        }

        if (dto.fullName() != null && !dto.fullName().isBlank()) {
            user.updateFullName(dto.fullName().trim());
        }

        if (dto.roleIds() != null) {
            List<Role> roles = roleRepository.findAllById(dto.roleIds());
            user.setRoles(new HashSet<>(roles));
        }

        User updated = userRepository.save(user);
        return new UserResponseDto(updated);
    }

    @Transactional
    public void deactivate(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + id));
        user.deactivate();
        userRepository.save(user);
    }

    @Transactional
    public void activate(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + id));
        user.activate();
        userRepository.save(user);
    }

    @Transactional
    public void suspend(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + id));
        user.suspend();
        userRepository.save(user);
    }

    private void runValidators(UserRequestDto dto, Long targetId) {
        for (UserValidator validator : validators) {
            validator.validate(dto, targetId);
        }
    }
}
