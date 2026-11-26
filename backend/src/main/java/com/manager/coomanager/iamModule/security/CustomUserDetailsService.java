package com.manager.coomanager.iamModule.security;

import com.manager.coomanager.shared.domain.valueobjects.Email;
import com.manager.coomanager.iamModule.entities.User;
import com.manager.coomanager.iamModule.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Serviço que carrega as credenciais e dados do usuário para o Spring Security.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(new Email(username))
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + username));
        return UserPrincipal.create(user);
    }
}

