package com.manager.coopafi.resources;

import com.manager.coopafi.dto.consumerunit.*;
import com.manager.coopafi.services.ConsumerUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/consumerunits")
public class ConsumerUnitResources {

    @Autowired
    private ConsumerUnitService service;

    @GetMapping
    public ResponseEntity<List<ConsumerUnitMinDto>> findAllByStatus() {
        List<ConsumerUnitMinDto> list = service.findAllByStatus();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ConsumerUnitDto> findByStatusAndId(@PathVariable Long id) {
        ConsumerUnitDto obj = service.findByStatusAndId(id);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    public ResponseEntity<ConsumerUnitDto> insert(@RequestBody ConsumerUnitInsertDto dto) {
        ConsumerUnitDto newConsumerUnit = service.insert(dto);
        return ResponseEntity.ok().body(newConsumerUnit);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<ConsumerUnitDto> update(@PathVariable Long id, @RequestBody ConsumerUnitUpdateDto dto) {
        return ResponseEntity.ok().body(service.update(id, dto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

