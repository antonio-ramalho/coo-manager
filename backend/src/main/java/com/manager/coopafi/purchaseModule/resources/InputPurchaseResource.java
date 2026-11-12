package com.manager.coopafi.purchaseModule.resources;

import com.manager.coopafi.purchaseModule.dto.inputPurchase.InputPurchaseDto;
import com.manager.coopafi.purchaseModule.dto.inputPurchase.InputPurchaseInsertDto;
import com.manager.coopafi.purchaseModule.dto.inputPurchase.InputPurchaseMinDto;
import com.manager.coopafi.purchaseModule.dto.inputPurchase.InputPurchaseTableDto;
import com.manager.coopafi.purchaseModule.dto.inputPurchase.RecentSaleDto;
import com.manager.coopafi.purchaseModule.services.interfaces.InputPurchaseService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.net.URI;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(value = "/purchase")
public class InputPurchaseResource {

    private final InputPurchaseService service;

    public InputPurchaseResource(InputPurchaseService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<InputPurchaseMinDto>> findAllByStatus() {
        List<InputPurchaseMinDto> list = service.findAllByStatus();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/paged")
    public ResponseEntity<Page<InputPurchaseTableDto>> findPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String searchTerm) {
        Page<InputPurchaseTableDto> result = service.searchPurchases(page, size, searchTerm);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/recent")
    public ResponseEntity<List<RecentSaleDto>> findRecent() {
        List<RecentSaleDto> list = service.findRecentPurchases();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<InputPurchaseDto> findById(@PathVariable Long id) {
        InputPurchaseDto obj = service.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    public ResponseEntity<InputPurchaseDto> insert(@RequestBody InputPurchaseInsertDto dto) {
        InputPurchaseDto newPurchase = service.processPurchase(dto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(newPurchase.id()).toUri();
        return ResponseEntity.created(uri).body(newPurchase);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<InputPurchaseDto> registerPayment(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.registerPayment(id));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> cancelPurchase(@PathVariable Long id) {
        service.cancelPurchase(id);
        return ResponseEntity.noContent().build();
    }
}
