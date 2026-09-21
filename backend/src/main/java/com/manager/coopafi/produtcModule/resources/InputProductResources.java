package com.manager.coopafi.produtcModule.resources;

import com.manager.coopafi.purchaseModule.dto.inputProduct.InputProductDto;
import com.manager.coopafi.purchaseModule.dto.inputProduct.InputProductInsertDto;
import com.manager.coopafi.purchaseModule.dto.inputProduct.InputProductMinDto;
import com.manager.coopafi.purchaseModule.dto.inputProduct.InputProductUpdateDto;
import com.manager.coopafi.produtcModule.services.InputProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/input-products")
public class InputProductResources {

    @Autowired
    private InputProductService service;

    @GetMapping
    public ResponseEntity<List<InputProductMinDto>> findAllByStatus() {
        List <InputProductMinDto> list = service.findAllByStatus();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<InputProductDto> findByStatusAndId(@PathVariable Long id) {
        InputProductDto obj = service.findByStatusAndId(id);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    public ResponseEntity<InputProductDto> insert(@RequestBody InputProductInsertDto dto) {
        InputProductDto newProduct = service.insert(dto);
        return ResponseEntity.ok().body(newProduct);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<InputProductDto> update(@PathVariable Long id, @RequestBody InputProductUpdateDto dto) {
        return ResponseEntity.ok().body(service.update(id, dto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
