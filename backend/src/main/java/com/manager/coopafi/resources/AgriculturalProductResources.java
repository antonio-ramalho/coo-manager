package com.manager.coopafi.resources;

import com.manager.coopafi.dto.agriculturalProduct.AgriculturalProductDto;
import com.manager.coopafi.dto.agriculturalProduct.AgriculturalProductInsertDto;
import com.manager.coopafi.dto.agriculturalProduct.AgriculturalProductMinDto;
import com.manager.coopafi.dto.agriculturalProduct.AgriculturalProductUpdateDto;
import com.manager.coopafi.services.AgriculturalProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/agricultural-products")
public class AgriculturalProductResources {

    @Autowired
    private AgriculturalProductService service;

    @GetMapping
    public ResponseEntity<List<AgriculturalProductMinDto>> findByStatus() {
        List <AgriculturalProductMinDto> list = service.findByStatus();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<AgriculturalProductDto> findByStatusAndId(@PathVariable Long id) {
        AgriculturalProductDto obj = service.findByStatusAndId(id);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    public ResponseEntity<AgriculturalProductDto> insert(@RequestBody AgriculturalProductInsertDto dto) {
        AgriculturalProductDto newFarmer = service.insert(dto);
        return ResponseEntity.ok().body(newFarmer);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<AgriculturalProductDto> update(@PathVariable Long id, @RequestBody AgriculturalProductUpdateDto dto) {
        return ResponseEntity.ok().body(service.update(id, dto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
