package com.manager.coopafi.resources;

import com.manager.coopafi.dto.inputPurchase.InputPurchaseDto;
import com.manager.coopafi.dto.inputPurchase.InputPurchaseInsertDto;
import com.manager.coopafi.dto.inputPurchase.InputPurchaseMinDto;
import com.manager.coopafi.services.InputPurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/purchase")
public class InputPurchaseResource {

    @Autowired
    private InputPurchaseService service;

    @GetMapping
    public ResponseEntity<List<InputPurchaseMinDto>> findAllByStatus() {
        List <InputPurchaseMinDto> list = service.findAllByStatus();
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
        return ResponseEntity.ok().body(newPurchase);
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
