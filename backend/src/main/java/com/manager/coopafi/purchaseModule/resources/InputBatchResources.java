package com.manager.coopafi.purchaseModule.resources;

import com.manager.coopafi.purchaseModule.dto.inputBatch.InputBatchDto;
import com.manager.coopafi.purchaseModule.dto.inputBatch.InputBatchInsertDto;
import com.manager.coopafi.purchaseModule.dto.inputBatch.InputBatchMinDto;
import com.manager.coopafi.purchaseModule.dto.inputBatch.InputBatchUpdateDto;
import com.manager.coopafi.purchaseModule.services.interfaces.InputBatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.manager.coopafi.purchaseModule.dto.inputBatch.InputBatchTableDto;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping(value = "/input-batches")
public class InputBatchResources {

    private final InputBatchService service;

    public InputBatchResources(InputBatchService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<InputBatchMinDto>> findAllByStatus() {
        List<InputBatchMinDto> list = service.findAllByStatus();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/paged")
    public ResponseEntity<Page<InputBatchTableDto>> findAllPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "") String searchTerm) {
        Page<InputBatchTableDto> result = service.searchBatches(page, size, searchTerm);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<InputBatchDto> findById(@PathVariable Long id) {
        InputBatchDto obj = service.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    public ResponseEntity<InputBatchDto> insert(@RequestBody InputBatchInsertDto dto) {
        InputBatchDto newBatch = service.insert(dto);
        return ResponseEntity.ok().body(newBatch);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<InputBatchDto> update(@PathVariable Long id, @RequestBody InputBatchUpdateDto dto) {
        return ResponseEntity.ok().body(service.update(id, dto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/{id}/activate")
    public ResponseEntity<Void> activateById(@PathVariable Long id) {
        service.activate(id);
        return ResponseEntity.noContent().build();
    }
}
