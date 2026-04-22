package com.manager.coopafi.resources;

import com.manager.coopafi.domain.entities.Farmer;
import com.manager.coopafi.services.FarmerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/farmers")
public class FarmerResources {

    @Autowired
    private FarmerService service;

    @GetMapping
    public ResponseEntity<List<Farmer>> findAll() {
        List <Farmer> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Farmer> findById(@PathVariable Long id) {
        Farmer obj = service.findById(id);
        return ResponseEntity.ok().body(obj);
    }
}
