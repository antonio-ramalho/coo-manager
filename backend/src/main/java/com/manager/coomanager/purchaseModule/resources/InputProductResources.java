package com.manager.coomanager.purchaseModule.resources;

import com.manager.coomanager.purchaseModule.dto.inputProduct.InputProductDto;
import com.manager.coomanager.purchaseModule.dto.inputProduct.InputProductInsertDto;
import com.manager.coomanager.purchaseModule.dto.inputProduct.InputProductMinDto;
import com.manager.coomanager.purchaseModule.dto.inputProduct.InputProductUpdateDto;
import com.manager.coomanager.purchaseModule.services.interfaces.InputProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

@RestController
@RequestMapping(value = "/input-products")
public class InputProductResources {

    private final InputProductService service;

    public InputProductResources(InputProductService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('INPUT_READ')")
    public ResponseEntity<Page<InputProductMinDto>> findAllByStatus(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "") String searchTerm) {
        Pageable pageable = PageRequest.of(page, size);
        Page<InputProductMinDto> list = service.findAllByPage(pageable, searchTerm);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('INPUT_READ')")
    public ResponseEntity<InputProductDto> findByStatusAndId(@PathVariable Long id) {
        InputProductDto obj = service.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('INPUT_WRITE')")
    public ResponseEntity<InputProductDto> insert(@RequestBody InputProductInsertDto dto) {
        InputProductDto newProduct = service.insert(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newProduct.id()).toUri();

        return ResponseEntity.created(uri).body(newProduct);
    }

    @PatchMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('INPUT_WRITE')")
    public ResponseEntity<InputProductDto> update(@PathVariable Long id, @RequestBody InputProductUpdateDto dto) {
        return ResponseEntity.ok().body(service.update(id, dto));
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('INPUT_WRITE')")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/{id}/activate")
    @PreAuthorize("hasAuthority('INPUT_WRITE')")
    public ResponseEntity<Void> activate(@PathVariable Long id) {
        service.activate(id);
        return ResponseEntity.noContent().build();
    }
}
