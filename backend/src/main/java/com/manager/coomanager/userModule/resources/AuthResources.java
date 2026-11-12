package com.manager.coomanager.userModule.resources;

import com.manager.coomanager.userModule.dto.*;
import com.manager.coomanager.userModule.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthResources {

    private final AuthService authService;

    public AuthResources(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto dto) {
        LoginResponseDto response = authService.login(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/2fa/verify")
    public ResponseEntity<LoginResponseDto> verifyTwoFactor(@RequestBody TotpVerifyRequestDto dto) {
        LoginResponseDto response = authService.verifyTwoFactor(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/2fa/setup")
    public ResponseEntity<TotpSetupResponseDto> setupTwoFactor() {
        TotpSetupResponseDto response = authService.setupTwoFactor();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/2fa/confirm")
    public ResponseEntity<Void> confirmTwoFactor(@RequestBody TotpConfirmRequestDto dto) {
        authService.confirmTwoFactor(dto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/2fa/disable")
    public ResponseEntity<Void> disableTwoFactor(@RequestParam String code) {
        authService.disableTwoFactor(code);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getAuthenticatedUser() {
        UserResponseDto response = authService.getAuthenticatedUser();
        return ResponseEntity.ok(response);
    }
}
