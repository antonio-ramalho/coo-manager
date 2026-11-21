package com.manager.coomanager.infrastructure.security;

import com.manager.coomanager.infrastructure.valueObjects.Email;
import com.manager.coomanager.personModule.enums.Status;
import com.manager.coomanager.userModule.entities.User;
import com.manager.coomanager.userModule.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * Manipulador de sucesso após autenticação OAuth2 (Google Login).
 * Implementa a restrição de cadastro fechado (sem open-registration):
 * se o e-mail não existir previamente cadastrado ou pertencer a cooperativa inativa,
 * o acesso é negado.
 */
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Value("${cors.allowed-origins:http://localhost:4200}")
    private String allowedOrigins;

    public OAuth2AuthenticationSuccessHandler(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String targetUrl = determineTargetUrl(authentication);
        if (response.isCommitted()) {
            return;
        }
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    @Transactional(readOnly = true)
    public String determineTargetUrl(Authentication authentication) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String baseUrl = allowedOrigins.split(",")[0].trim();

        if (email == null || email.isBlank()) {
            return UriComponentsBuilder.fromUriString(baseUrl + "/login")
                    .queryParam("error", "oauth2_email_not_provided")
                    .queryParam("message", URLEncoder.encode("E-mail não fornecido pelo Google.", StandardCharsets.UTF_8))
                    .build().toUriString();
        }

        Optional<User> optionalUser = userRepository.findByEmailWithCooperative(new Email(email));
        if (optionalUser.isEmpty()) {
            return UriComponentsBuilder.fromUriString(baseUrl + "/login")
                    .queryParam("error", "access_denied")
                    .queryParam("message", URLEncoder.encode("Acesso negado. Seu e-mail não possui cadastro ou convite em nenhuma cooperativa.", StandardCharsets.UTF_8))
                    .build().toUriString();
        }

        User user = optionalUser.get();
        if (user.getStatus() != Status.ACTIVE ||
                user.getCooperative() == null ||
                user.getCooperative().getStatus() != Status.ACTIVE) {
            return UriComponentsBuilder.fromUriString(baseUrl + "/login")
                    .queryParam("error", "access_denied")
                    .queryParam("message", URLEncoder.encode("Conta de usuário ou cooperativa inativa no sistema.", StandardCharsets.UTF_8))
                    .build().toUriString();
        }

        if (user.isTwoFactorEnabled()) {
            String preAuthToken = jwtService.generatePreAuthToken(user);
            return UriComponentsBuilder.fromUriString(baseUrl + "/login")
                    .queryParam("twoFactorRequired", "true")
                    .queryParam("preAuthToken", preAuthToken)
                    .build().toUriString();
        }

        String accessToken = jwtService.generateAccessToken(user);
        return UriComponentsBuilder.fromUriString(baseUrl + "/login")
                .queryParam("token", accessToken)
                .build().toUriString();
    }
}

