package com.manager.coomanager.iamModule.resources;

import com.manager.coomanager.iamModule.dto.RoleRequestDto;
import com.manager.coomanager.iamModule.dto.RoleResponseDto;
import com.manager.coomanager.iamModule.services.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/roles")
@PreAuthorize("hasAuthority('USER_MANAGE')")
public class RoleResources {

    private final RoleService roleService;

    public RoleResources(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/cooperative/{cooperativeId}")
    public ResponseEntity<List<RoleResponseDto>> findAllForCooperative(@PathVariable Long cooperativeId) {
        return ResponseEntity.ok(roleService.findAllForCooperative(cooperativeId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.findById(id));
    }

    @PostMapping
    public ResponseEntity<RoleResponseDto> insert(@RequestBody RoleRequestDto dto) {
        RoleResponseDto created = roleService.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(uri).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleResponseDto> update(@PathVariable Long id, @RequestBody RoleRequestDto dto) {
        RoleResponseDto updated = roleService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
