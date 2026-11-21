package com.manager.coomanager.userModule.resources;

import com.manager.coomanager.userModule.dto.UserMinDto;
import com.manager.coomanager.userModule.dto.UserRequestDto;
import com.manager.coomanager.userModule.dto.UserResponseDto;
import com.manager.coomanager.userModule.dto.UserUpdateDto;
import com.manager.coomanager.userModule.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

@RestController
@RequestMapping("/users")
public class UserResources {

    private final UserService userService;

    public UserResources(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/cooperative/{cooperativeId}")
    @PreAuthorize("hasAuthority('USER_MANAGE')")
    public ResponseEntity<Page<UserMinDto>> findAllByCooperative(
            @PathVariable Long cooperativeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "") String searchTerm) {

        Pageable pageable = PageRequest.of(page, size);
        Page<UserMinDto> result = userService.findAllByCooperative(cooperativeId, pageable, searchTerm);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_MANAGE') or (authentication.principal != null and authentication.principal.id == #id)")
    public ResponseEntity<UserResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER_MANAGE')")
    public ResponseEntity<UserResponseDto> insert(@RequestBody UserRequestDto dto) {
        UserResponseDto created = userService.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(uri).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_MANAGE') or (authentication.principal != null and authentication.principal.id == #id)")
    public ResponseEntity<UserResponseDto> update(@PathVariable Long id, @RequestBody UserUpdateDto dto) {
        UserResponseDto updated = userService.update(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(updated);
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('USER_MANAGE')")
    public ResponseEntity<Void> activate(@PathVariable Long id) {
        userService.activate(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('USER_MANAGE')")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        userService.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    @PreAuthorize("hasAuthority('USER_MANAGE')")
    public ResponseEntity<Void> suspend(@PathVariable Long id) {
        userService.suspend(id);
        return ResponseEntity.noContent().build();
    }
}
