package com.manager.coomanager.iamModule.security;

import com.manager.coomanager.iamModule.entities.Role;
import com.manager.coomanager.iamModule.entities.User;
import com.manager.coomanager.iamModule.enums.Permission;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço responsável pela emissão, decodificação e validação de tokens JWT.
 * O tempo de expiração é flexível conforme o perfil ativo (dev = 30 dias, prod = 2 horas).
 */
@Service
public class JwtService {

    public static final String CLAIM_USER_ID = "userId";
    public static final String CLAIM_COOPERATIVE_ID = "cooperativeId";
    public static final String CLAIM_COOPERATIVE_NAME = "cooperativeName";
    public static final String CLAIM_FULL_NAME = "fullName";
    public static final String CLAIM_ROLES = "roles";
    public static final String CLAIM_PERMISSIONS = "permissions";
    public static final String CLAIM_SCOPE = "scope";

    public static final String SCOPE_ACCESS = "ACCESS";
    public static final String SCOPE_PRE_AUTH = "2FA_PENDING";

    private final SecretKey signingKey;
    private final long expirationMs;
    private final long preAuthExpirationMs = 300_000L; // 5 minutos para conclusão do 2FA

    public JwtService(@Value("${security.jwt.secret}") String secret,
                      @Value("${security.jwt.expiration-ms}") long expirationMs) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    /**
     * Emite um token de acesso completo com as credenciais e permissões do usuário.
     */
    public String generateAccessToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);

        List<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        List<String> permissions = user.getRoles().stream()
                .flatMap(r -> r.getPermissions().stream())
                .map(Permission::name)
                .distinct()
                .collect(Collectors.toList());

        Long coopId = user.getCooperative() != null ? user.getCooperative().getId() : null;
        String coopName = user.getCooperative() != null ? user.getCooperative().getTradeName() : null;

        return Jwts.builder()
                .subject(user.getEmail().getAddressEmail())
                .claim(CLAIM_USER_ID, user.getId())
                .claim(CLAIM_COOPERATIVE_ID, coopId)
                .claim(CLAIM_COOPERATIVE_NAME, coopName)
                .claim(CLAIM_FULL_NAME, user.getFullName())
                .claim(CLAIM_ROLES, roleNames)
                .claim(CLAIM_PERMISSIONS, permissions)
                .claim(CLAIM_SCOPE, SCOPE_ACCESS)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(signingKey)
                .compact();
    }

    /**
     * Emite um token parcial (pre-auth) para validação do segundo fator (2FA/TOTP).
     * Possui escopo restrito e validade curta.
     */
    public String generatePreAuthToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + preAuthExpirationMs);

        Long coopId = user.getCooperative() != null ? user.getCooperative().getId() : null;

        return Jwts.builder()
                .subject(user.getEmail().getAddressEmail())
                .claim(CLAIM_USER_ID, user.getId())
                .claim(CLAIM_COOPERATIVE_ID, coopId)
                .claim(CLAIM_SCOPE, SCOPE_PRE_AUTH)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(signingKey)
                .compact();
    }

    /**
     * Valida a assinatura e a expiração do token.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Long extractUserId(String token) {
        return extractAllClaims(token).get(CLAIM_USER_ID, Long.class);
    }

    public Long extractCooperativeId(String token) {
        return extractAllClaims(token).get(CLAIM_COOPERATIVE_ID, Long.class);
    }

    public String extractScope(String token) {
        return extractAllClaims(token).get(CLAIM_SCOPE, String.class);
    }

    public boolean isPreAuthToken(String token) {
        return SCOPE_PRE_AUTH.equals(extractScope(token));
    }
}

