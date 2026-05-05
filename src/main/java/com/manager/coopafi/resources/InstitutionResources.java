package com.manager.coopafi.resources;

import com.manager.coopafi.dto.institution.InstitutionDto;
import com.manager.coopafi.dto.institution.InstitutionInsertDto;
import com.manager.coopafi.dto.institution.InstitutionMinDto;
import com.manager.coopafi.dto.institution.InstitutionUpdateDto;
import com.manager.coopafi.services.InstitutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/institutions")
public class InstitutionResources {

    @Autowired
    private InstitutionService service;

    @GetMapping
    public ResponseEntity<List<InstitutionMinDto>> findByStatus() {
        List<InstitutionMinDto> list = service.findByStatus();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<InstitutionDto> findByStatusAndId(@PathVariable Long id) {
        InstitutionDto obj = service.findByStatusAndId(id);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    public ResponseEntity<InstitutionDto> insert(@RequestBody InstitutionInsertDto dto) {
        InstitutionDto newInstitution = service.insert(dto);
        return ResponseEntity.ok().body(newInstitution);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<InstitutionDto> update(@PathVariable Long id, @RequestBody InstitutionUpdateDto dto) {
        return ResponseEntity.ok().body(service.update(id, dto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

