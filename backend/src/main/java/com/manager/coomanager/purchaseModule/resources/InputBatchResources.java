package com.manager.coomanager.purchaseModule.resources;

import com.manager.coomanager.purchaseModule.dto.inputBatch.InputBatchDto;
import com.manager.coomanager.purchaseModule.dto.inputBatch.InputBatchInsertDto;
import com.manager.coomanager.purchaseModule.dto.inputBatch.InputBatchMinDto;
import com.manager.coomanager.purchaseModule.dto.inputBatch.InputBatchTableDto;
import com.manager.coomanager.purchaseModule.dto.inputBatch.InputBatchUpdateDto;
import com.manager.coomanager.purchaseModule.services.interfaces.InputBatchService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/input-batches")
public class InputBatchResources {

    private final InputBatchService service;

    public InputBatchResources(InputBatchService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('INPUT_READ') or hasAuthority('SALES_WRITE')")
    public ResponseEntity<List<InputBatchMinDto>> findAllByStatus() {
        List<InputBatchMinDto> list = service.findAllByStatus();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/paged")
    @PreAuthorize("hasAuthority('INPUT_READ') or hasAuthority('SALES_WRITE')")
    public ResponseEntity<Page<InputBatchTableDto>> findAllPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "") String searchTerm,
            @RequestParam(defaultValue = "false") boolean onlyInStock) {
        Page<InputBatchTableDto> result = service.searchBatches(page, size, searchTerm, onlyInStock);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('INPUT_READ')")
    public ResponseEntity<InputBatchDto> findById(@PathVariable Long id) {
        InputBatchDto obj = service.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('INPUT_WRITE')")
    public ResponseEntity<InputBatchDto> insert(@RequestBody InputBatchInsertDto dto) {
        InputBatchDto newBatch = service.insert(dto);
        return ResponseEntity.ok().body(newBatch);
    }

    @PatchMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('INPUT_WRITE')")
    public ResponseEntity<InputBatchDto> update(@PathVariable Long id, @RequestBody InputBatchUpdateDto dto) {
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
    public ResponseEntity<Void> activateById(@PathVariable Long id) {
        service.activate(id);
        return ResponseEntity.noContent().build();
    }
}
