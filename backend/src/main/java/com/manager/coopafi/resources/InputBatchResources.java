package com.manager.coopafi.resources;

import com.manager.coopafi.dto.inputBatch.InputBatchDto;
import com.manager.coopafi.dto.inputBatch.InputBatchInsertDto;
import com.manager.coopafi.dto.inputBatch.InputBatchMinDto;
import com.manager.coopafi.dto.inputBatch.InputBatchUpdateDto;
import com.manager.coopafi.services.InputBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/input-batch")
public class InputBatchResources {

    @Autowired
    private InputBatchService service;

    @GetMapping
    public ResponseEntity<List<InputBatchMinDto>> findAllByStatus() {
        List <InputBatchMinDto> list = service.findAllByStatus();
        return ResponseEntity.ok().body(list);
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
}
