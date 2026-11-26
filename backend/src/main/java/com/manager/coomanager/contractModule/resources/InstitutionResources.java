package com.manager.coomanager.contractModule.resources;

import com.manager.coomanager.contractModule.dto.institution.InstitutionDto;
import com.manager.coomanager.contractModule.dto.institution.InstitutionInsertDto;
import com.manager.coomanager.contractModule.dto.institution.InstitutionMinDto;
import com.manager.coomanager.contractModule.dto.institution.InstitutionUpdateDto;
import com.manager.coomanager.contractModule.services.InstitutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/institutions")
public class InstitutionResources {

    @Autowired
    private InstitutionService service;

    @GetMapping
    @PreAuthorize("hasAuthority('CONTRACT_READ')")
    public ResponseEntity<List<InstitutionMinDto>> findAllByStatus() {
        List<InstitutionMinDto> list = service.findAllByStatus();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('CONTRACT_READ')")
    public ResponseEntity<InstitutionDto> findByStatusAndId(@PathVariable Long id) {
        InstitutionDto obj = service.findByStatusAndId(id);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CONTRACT_WRITE')")
    public ResponseEntity<InstitutionDto> insert(@RequestBody InstitutionInsertDto dto) {
        InstitutionDto newInstitution = service.insert(dto);
        return ResponseEntity.ok().body(newInstitution);
    }

    @PatchMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('CONTRACT_WRITE')")
    public ResponseEntity<InstitutionDto> update(@PathVariable Long id, @RequestBody InstitutionUpdateDto dto) {
        return ResponseEntity.ok().body(service.update(id, dto));
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('CONTRACT_WRITE')")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

