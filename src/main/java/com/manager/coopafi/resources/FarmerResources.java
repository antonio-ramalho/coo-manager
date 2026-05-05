package com.manager.coopafi.resources;

import com.manager.coopafi.domain.entities.Farmer;
import com.manager.coopafi.domain.entities.NaturalPerson;
import com.manager.coopafi.dto.FarmerDto;
import com.manager.coopafi.dto.FarmerInsertDTO;
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
    public ResponseEntity<List<FarmerDto>> findAll() {
        List <FarmerDto> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<FarmerDto> findById(@PathVariable Long id) {
        FarmerDto obj = service.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    public ResponseEntity<FarmerDto> insert(@RequestBody FarmerInsertDTO dto) {
        FarmerDto newFarmer = service.insert(dto);
        return ResponseEntity.ok().body(newFarmer);
    }
}
