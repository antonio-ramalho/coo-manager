package com.manager.coopafi.contractModule.resources;

import com.manager.coopafi.contractModule.dto.contract.ContractDto;
import com.manager.coopafi.contractModule.dto.contract.ContractInsertDto;
import com.manager.coopafi.contractModule.dto.contract.ContractMinDto;
import com.manager.coopafi.contractModule.dto.contract.ContractUpdateDto;
import com.manager.coopafi.contractModule.services.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/contract")
public class ContractResources {

    @Autowired
    private ContractService service;

    @GetMapping
    public ResponseEntity<List<ContractMinDto>> findAll() {
        List<ContractMinDto> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ContractDto> findById(@PathVariable Long id) {
        ContractDto obj = service.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    public ResponseEntity<ContractDto> create(@RequestBody ContractInsertDto dto) {
        ContractDto newContract = service.create(dto);
        return ResponseEntity.ok().body(newContract);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<ContractDto> update(@PathVariable Long id, @RequestBody ContractUpdateDto dto) {
        ContractDto obj = service.update(id, dto);
        return ResponseEntity.ok().body(obj);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void>  delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}