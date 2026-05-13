package com.manager.coopafi.services;

import com.manager.coopafi.domain.entities.*;
import com.manager.coopafi.domain.valueObjects.Quantity;
import com.manager.coopafi.dto.inputPurchase.InputPurchaseDto;
import com.manager.coopafi.dto.inputPurchase.InputPurchaseInsertDto;
import com.manager.coopafi.dto.inputPurchase.InputPurchaseMinDto;
import com.manager.coopafi.dto.inputPurchaseItem.InputPurchaseItemInsertDto;
import com.manager.coopafi.enums.PaymentStatus;
import com.manager.coopafi.exceptions.DomainException;
import com.manager.coopafi.repositories.FarmerRepository;
import com.manager.coopafi.repositories.InputBatchRepository;
import com.manager.coopafi.repositories.InputPurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class InputPurchaseService {

    @Autowired
    private InputPurchaseRepository purchaseRepository;

    @Autowired
    private FarmerRepository farmerRepository;

    @Autowired
    private InputBatchRepository batchRepository;

    @Transactional(readOnly = true)
    public List<InputPurchaseMinDto> findAllByStatus() {
        List<InputPurchase> list = purchaseRepository.findAllByStatus(PaymentStatus.PAID);
        return list.stream().map(InputPurchaseMinDto::new).toList();
    }

    @Transactional(readOnly = true)
    public InputPurchaseDto findById(Long id) {
        InputPurchase purchase = purchaseRepository.findById(id).orElseThrow(() ->
                new DomainException("Venda não encontrada"));
        return new InputPurchaseDto(purchase);
    }

    @Transactional
    public InputPurchaseDto processPurchase(InputPurchaseInsertDto dto) {

        if (dto.items() == null || dto.items().isEmpty()) {
            throw new DomainException("É necessário que existe pelo menos um item.");
        }

        Farmer farmer = farmerRepository.findById(dto.farmerId())
                .orElseThrow(() -> new DomainException("Agricultor não encontrado."));

        InputPurchase purchase = new InputPurchase(farmer);

        for (InputPurchaseItemInsertDto itemDto : dto.items()) {
            InputBatch batch = batchRepository.findById(itemDto.batchId())
                .orElseThrow(() -> new DomainException("Produto não existente no estoque."));

            Quantity quantity = new Quantity(new BigDecimal(itemDto.quantity()));
            InputPurchaseItem item = new InputPurchaseItem(quantity, batch.getInputProduct().getProductPrice(), batch);

            purchase.addItem(item);
        }

        purchase = purchaseRepository.save(purchase);

        return new InputPurchaseDto(purchase);
    }

    @Transactional
    public InputPurchaseDto registerPayment(Long id) {
        InputPurchase purchase = purchaseRepository.findById(id)
            .orElseThrow(() -> new DomainException("Venda não encontrada."));

        purchase.markAsPaid();
        purchaseRepository.save(purchase);
        return new InputPurchaseDto(purchase);
    }

    @Transactional
    public void cancelPurchase(Long id) {
        InputPurchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new DomainException("Venda não encontrada."));

        purchase.cancelPurchase();
        purchaseRepository.save(purchase);
    }
}
