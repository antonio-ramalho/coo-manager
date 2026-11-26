package com.manager.coomanager.contractModule.resources;

import com.manager.coomanager.contractModule.dto.consumerunit.ConsumerUnitDto;
import com.manager.coomanager.contractModule.dto.consumerunit.ConsumerUnitInsertDto;
import com.manager.coomanager.contractModule.dto.consumerunit.ConsumerUnitMinDto;
import com.manager.coomanager.contractModule.dto.consumerunit.ConsumerUnitUpdateDto;
import com.manager.coomanager.contractModule.services.ConsumerUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/consumerunits")
public class ConsumerUnitResources {

    @Autowired
    private ConsumerUnitService service;

    @GetMapping
    @PreAuthorize("hasAuthority('CONTRACT_READ')")
    public ResponseEntity<List<ConsumerUnitMinDto>> findAllByStatus() {
        List<ConsumerUnitMinDto> list = service.findAllByStatus();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('CONTRACT_READ')")
    public ResponseEntity<ConsumerUnitDto> findByStatusAndId(@PathVariable Long id) {
        ConsumerUnitDto obj = service.findByStatusAndId(id);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CONTRACT_WRITE')")
    public ResponseEntity<ConsumerUnitDto> insert(@RequestBody ConsumerUnitInsertDto dto) {
        ConsumerUnitDto newConsumerUnit = service.insert(dto);
        return ResponseEntity.ok().body(newConsumerUnit);
    }

    @PatchMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('CONTRACT_WRITE')")
    public ResponseEntity<ConsumerUnitDto> update(@PathVariable Long id, @RequestBody ConsumerUnitUpdateDto dto) {
        return ResponseEntity.ok().body(service.update(id, dto));
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('CONTRACT_WRITE')")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

