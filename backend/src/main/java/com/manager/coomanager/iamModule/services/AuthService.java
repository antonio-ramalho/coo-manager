package com.manager.coomanager.iamModule.services;

import com.manager.coomanager.shared.domain.exceptions.DomainException;
import com.manager.coomanager.shared.domain.exceptions.ResourceNotFoundException;
import com.manager.coomanager.iamModule.security.JwtService;
import com.manager.coomanager.iamModule.security.TotpService;
import com.manager.coomanager.shared.domain.valueobjects.Email;
import com.manager.coomanager.personModule.enums.Status;
import com.manager.coomanager.iamModule.dto.*;
import com.manager.coomanager.iamModule.entities.User;
import com.manager.coomanager.iamModule.repositories.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TotpService totpService;

    public AuthService(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            TotpService totpService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.totpService = totpService;
    }

    @Transactional(readOnly = true)
    public LoginResponseDto login(LoginRequestDto dto) {
        if (dto.email() == null || dto.password() == null) {
            throw new BadCredentialsException("E-mail e senha são obrigatórios.");
        }

        User user = userRepository.findByEmail(new Email(dto.email()))
                .orElseThrow(() -> new BadCredentialsException("E-mail ou senha incorretos."));

        if (user.getPasswordHash() == null ||
                !passwordEncoder.matches(dto.password(), user.getPasswordHash().getHash())) {
            throw new BadCredentialsException("E-mail ou senha incorretos.");
        }

        if (user.getStatus() != Status.ACTIVE) {
            throw new DomainException("Acesso negado. A conta do usuário não está ativa.");
        }

        if (user.isTwoFactorEnabled()) {
            String preAuthToken = jwtService.generatePreAuthToken(user);
            return new LoginResponseDto(true, preAuthToken, null, null);
        }

        String accessToken = jwtService.generateAccessToken(user);
        return new LoginResponseDto(false, null, accessToken, new UserResponseDto(user));
    }

    @Transactional(readOnly = true)
    public LoginResponseDto verifyTwoFactor(TotpVerifyRequestDto dto) {
        if (dto.preAuthToken() == null || dto.code() == null) {
            throw new BadCredentialsException("Token parcial e código TOTP são obrigatórios.");
        }

        if (!jwtService.validateToken(dto.preAuthToken()) || !jwtService.isPreAuthToken(dto.preAuthToken())) {
            throw new BadCredentialsException(
                    "Sessão de autenticação em duas etapas expirada ou inválida. Faça login novamente.");
        }

        String email = jwtService.extractEmail(dto.preAuthToken());
        User user = userRepository.findByEmail(new Email(email))
                .orElseThrow(() -> new BadCredentialsException("Usuário não encontrado."));

        if (!user.isTwoFactorEnabled() || user.getTwoFactorSecret() == null) {
            throw new DomainException("O segundo fator de autenticação não está configurado para esta conta.");
        }

        boolean isValid = totpService.verifyCode(user.getTwoFactorSecret(), dto.code());
        if (!isValid) {
            throw new BadCredentialsException("Código 2FA incorreto ou expirado.");
        }

        String accessToken = jwtService.generateAccessToken(user);
        return new LoginResponseDto(false, null, accessToken, new UserResponseDto(user));
    }

    @Transactional(readOnly = true)
    public TotpSetupResponseDto setupTwoFactor() {
        User user = getAuthenticatedUserEntity();
        String secret = totpService.generateSecret();
        String qrCodeUri = totpService.getQrCodeUri(secret, user.getEmail().getAddressEmail());
        return new TotpSetupResponseDto(secret, qrCodeUri);
    }

    @Transactional
    public void confirmTwoFactor(TotpConfirmRequestDto dto) {
        User user = getAuthenticatedUserEntity();

        boolean isValid = totpService.verifyCode(dto.secret(), dto.code());
        if (!isValid) {
            throw new DomainException("Código TOTP inválido. Verifique o aplicativo autenticador e tente novamente.");
        }

        user.enableTwoFactor(dto.secret());
        userRepository.save(user);
    }

    @Transactional
    public void disableTwoFactor(String code) {
        User user = getAuthenticatedUserEntity();

        if (!user.isTwoFactorEnabled()) {
            throw new DomainException("O 2FA já se encontra desativado.");
        }

        boolean isValid = totpService.verifyCode(user.getTwoFactorSecret(), code);
        if (!isValid) {
            throw new DomainException("Código TOTP incorreto. Não foi possível desativar o 2FA.");
        }

        user.disableTwoFactor();
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserResponseDto getAuthenticatedUser() {
        User user = getAuthenticatedUserEntity();
        return new UserResponseDto(user);
    }

    private User getAuthenticatedUserEntity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new DomainException("Nenhum usuário autenticado no contexto atual.");
        }

        String email = authentication.getName();
        return userRepository.findByEmail(new Email(email))
                .orElseThrow(
                        () -> new ResourceNotFoundException("Usuário autenticado não encontrado no banco de dados."));
    }
}
