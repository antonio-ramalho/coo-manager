package com.manager.coomanager.cooperativeModule.resources;

import com.manager.coomanager.cooperativeModule.dto.CooperativeMinDto;
import com.manager.coomanager.cooperativeModule.dto.CooperativeRequestDto;
import com.manager.coomanager.cooperativeModule.dto.CooperativeResponseDto;
import com.manager.coomanager.cooperativeModule.services.CooperativeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/cooperatives")
public class CooperativeResources {

    private final CooperativeService service;

    public CooperativeResources(CooperativeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<CooperativeMinDto>> findAllPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "") String searchTerm) {

        Pageable pageable = PageRequest.of(page, size);
        Page<CooperativeMinDto> result = service.findAllPaged(pageable, searchTerm);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CooperativeResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<CooperativeResponseDto> insert(@RequestBody CooperativeRequestDto dto) {
        CooperativeResponseDto created = service.insert(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.id()).toUri();

        return ResponseEntity.created(uri).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CooperativeResponseDto> update(@PathVariable Long id,
            @RequestBody CooperativeRequestDto dto) {
        CooperativeResponseDto updated = service.update(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(updated);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        service.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable Long id) {
        service.activate(id);
        return ResponseEntity.noContent().build();
    }
}
