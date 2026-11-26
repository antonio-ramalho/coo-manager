package com.manager.coomanager.iamModule.services;

import com.manager.coomanager.cooperativeModule.entities.Cooperative;
import com.manager.coomanager.cooperativeModule.repositories.CooperativeRepository;
import com.manager.coomanager.shared.domain.exceptions.DomainException;
import com.manager.coomanager.shared.domain.exceptions.ResourceNotFoundException;
import com.manager.coomanager.iamModule.security.UserPrincipal;
import com.manager.coomanager.shared.domain.valueobjects.Email;
import com.manager.coomanager.shared.domain.valueobjects.PasswordHash;
import com.manager.coomanager.iamModule.dto.ChangePasswordDto;
import com.manager.coomanager.iamModule.dto.UserMinDto;
import com.manager.coomanager.iamModule.dto.UserRequestDto;
import com.manager.coomanager.iamModule.dto.UserResponseDto;
import com.manager.coomanager.iamModule.dto.UserUpdateDto;
import com.manager.coomanager.iamModule.entities.Role;
import com.manager.coomanager.iamModule.entities.User;
import com.manager.coomanager.iamModule.enums.Permission;
import com.manager.coomanager.iamModule.repositories.RoleRepository;
import com.manager.coomanager.iamModule.repositories.UserRepository;
import com.manager.coomanager.iamModule.services.validators.UserValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

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

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            boolean hasUserManage = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals(Permission.USER_MANAGE.name()));
            Long currentUserId = (auth.getPrincipal() instanceof UserPrincipal p) ? p.getId() : null;
            boolean isSelfUpdate = Objects.equals(id, currentUserId);

            if (!hasUserManage && !isSelfUpdate) {
                throw new AccessDeniedException(
                        "Acesso negado: você não possui permissão para atualizar dados de outro usuário.");
            }

            if (dto.roleIds() != null) {
                if (!hasUserManage) {
                    throw new DomainException("Você não possui permissão para alterar cargos.");
                }
                if (isSelfUpdate) {
                    throw new DomainException(
                            "Não é permitido alterar o próprio cargo para prevenir auto-escalonamento ou bloqueio acidental.");
                }
            }
        }

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
    public void changePassword(Long id, ChangePasswordDto dto) {
        if (!dto.newPassword().equals(dto.confirmPassword())) {
            throw new DomainException("A nova senha e a confirmação de senha não conferem.");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + id));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal p) {
            if (!Objects.equals(id, p.getId())) {
                throw new AccessDeniedException("Você só tem permissão para alterar a sua própria senha.");
            }
        }

        if (!passwordEncoder.matches(dto.currentPassword(), user.getPasswordHash().getHash())) {
            throw new DomainException("A senha atual informada está incorreta.");
        }

        if (passwordEncoder.matches(dto.newPassword(), user.getPasswordHash().getHash())) {
            throw new DomainException("A nova senha não pode ser idêntica à senha atual.");
        }

        user.updatePassword(new PasswordHash(passwordEncoder.encode(dto.newPassword())));
        userRepository.save(user);
    }

    @Transactional
    public void deactivate(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + id));
        validateNotSelfOperation(id, "inativar");
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
        validateNotSelfOperation(id, "suspender");
        user.suspend();
        userRepository.save(user);
    }

    private void validateNotSelfOperation(Long targetId, String action) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal p) {
            if (Objects.equals(targetId, p.getId())) {
                throw new DomainException("Não é permitido " + action + " a própria conta.");
            }
        }
    }

    private void runValidators(UserRequestDto dto, Long targetId) {
        for (UserValidator validator : validators) {
            validator.validate(dto, targetId);
        }
    }
}
