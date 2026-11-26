package com.manager.coomanager.productModule.resources;

import com.manager.coomanager.productModule.dto.AgriculturalProductDto;
import com.manager.coomanager.productModule.dto.AgriculturalProductInsertDto;
import com.manager.coomanager.productModule.dto.AgriculturalProductMinDto;
import com.manager.coomanager.productModule.dto.AgriculturalProductUpdateDto;
import com.manager.coomanager.productModule.services.AgriculturalProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/agricultural-products")
public class AgriculturalProductResources {

    @Autowired
    private AgriculturalProductService service;

    @GetMapping
    @PreAuthorize("hasAuthority('CONTRACT_READ')")
    public ResponseEntity<List<AgriculturalProductMinDto>> findAllByStatus() {
        List <AgriculturalProductMinDto> list = service.findAllByStatus();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('CONTRACT_READ')")
    public ResponseEntity<AgriculturalProductDto> findByStatusAndId(@PathVariable Long id) {
        AgriculturalProductDto obj = service.findByStatusAndId(id);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CONTRACT_WRITE')")
    public ResponseEntity<AgriculturalProductDto> insert(@RequestBody AgriculturalProductInsertDto dto) {
        AgriculturalProductDto newFarmer = service.insert(dto);
        return ResponseEntity.ok().body(newFarmer);
    }

    @PatchMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('CONTRACT_WRITE')")
    public ResponseEntity<AgriculturalProductDto> update(@PathVariable Long id, @RequestBody AgriculturalProductUpdateDto dto) {
        return ResponseEntity.ok().body(service.update(id, dto));
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('CONTRACT_WRITE')")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
