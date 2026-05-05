package com.manager.coopafi.resources;

import com.manager.coopafi.dto.farmer.FarmerDto;
import com.manager.coopafi.dto.farmer.FarmerInsertDto;
import com.manager.coopafi.dto.farmer.FarmerMinDto;
import com.manager.coopafi.dto.farmer.FarmerUpdateDto;
import com.manager.coopafi.services.FarmerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/farmers")
public class FarmerResources {

    @Autowired
    private FarmerService service;

    @GetMapping
    public ResponseEntity<List<FarmerMinDto>> findByStatus() {
        List <FarmerMinDto> list = service.findByStatus();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<FarmerDto> findByStatusAndId(@PathVariable Long id) {
        FarmerDto obj = service.findByStatusAndId(id);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    public ResponseEntity<FarmerDto> insert(@RequestBody FarmerInsertDto dto) {
        FarmerDto newFarmer = service.insert(dto);
        return ResponseEntity.ok().body(newFarmer);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<FarmerDto> update(@PathVariable Long id, @RequestBody FarmerUpdateDto dto) {
        return ResponseEntity.ok().body(service.update(id, dto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
