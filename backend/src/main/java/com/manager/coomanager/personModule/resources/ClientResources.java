package com.manager.coomanager.personModule.resources;

import com.manager.coomanager.personModule.dto.client.ClientMinDto;
import com.manager.coomanager.personModule.interfaces.IClientDto;
import com.manager.coomanager.personModule.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

@RestController
@RequestMapping("/clients")
public class ClientResources {

    @Autowired
    private ClientService service;

    @GetMapping
    @PreAuthorize("hasAuthority('CLIENT_READ')")
    public ResponseEntity<Page<ClientMinDto>> findAllPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "") String searchTerm) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ClientMinDto> list = service.findAllByPage(pageable, searchTerm);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CLIENT_READ')")
    public ResponseEntity<IClientDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CLIENT_WRITE')")
    public ResponseEntity<IClientDto> insert(@RequestBody IClientDto dto) {
        IClientDto newDto = service.insert(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newDto.id()).toUri();

        return ResponseEntity.created(uri).body(newDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('CLIENT_WRITE')")
    public ResponseEntity<IClientDto> update(@PathVariable Long id, @RequestBody IClientDto dto) {
        IClientDto updatedDto = service.update(id, dto);
        return ResponseEntity.ok(updatedDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('CLIENT_WRITE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('CLIENT_WRITE')")
    public ResponseEntity<Void> activate(@PathVariable Long id) {
        service.activate(id);
        return ResponseEntity.noContent().build();
    }
}
