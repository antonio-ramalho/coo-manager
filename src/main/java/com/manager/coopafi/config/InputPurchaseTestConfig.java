package com.manager.coopafi.config;

import com.manager.coopafi.domain.entities.Farmer;
import com.manager.coopafi.domain.entities.InputBatch;
import com.manager.coopafi.domain.entities.InputPurchase;
import com.manager.coopafi.domain.entities.InputPurchaseItem;
import com.manager.coopafi.domain.valueObjects.Price;
import com.manager.coopafi.domain.valueObjects.Quantity;
import com.manager.coopafi.repositories.FarmerRepository;
import com.manager.coopafi.repositories.InputBatchRepository;
import com.manager.coopafi.repositories.InputPurchaseRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

import java.math.BigDecimal;

@Configuration
@Profile("test")
@Order(10)
public class InputPurchaseTestConfig implements CommandLineRunner {

    @Autowired
    private InputPurchaseRepository inputPurchaseRepository;

    @Autowired
    private FarmerRepository farmerRepository;

    @Autowired
    private InputBatchRepository inputBatchRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Buscar um farmer existente
        Farmer farmer = farmerRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Nenhum farmer encontrado"));

        // Buscar um inputBatch existente para muda de tomate
        InputBatch batchMudas = inputBatchRepository.findAll().stream()
                .filter(b -> b.getInputProduct().getProductCode().equals("MUD-TOM-001"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("InputBatch para MUD-TOM-001 não encontrado"));

        InputBatch batchMudas2 = inputBatchRepository.findAll().stream()
                .filter(b -> b.getInputProduct().getProductCode().equals("ADU-NPK-003"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("InputBatch para ADU-NPK-003 não encontrado"));

        // Criar a compra
        InputPurchase purchase = new InputPurchase(farmer);

        // Criar item 1 da compra
        Quantity quantity = new Quantity(BigDecimal.valueOf(50)); // Comprando 50 mudas
        Price appliedPrice = batchMudas.getInputProduct().getProductPrice() ; // Preço aplicado
        InputPurchaseItem item = new InputPurchaseItem(quantity, appliedPrice, batchMudas);

        // Criar item 2 da compra
        Quantity quantity2 = new Quantity(BigDecimal.valueOf(20)); // Comprando 20 Kg
        Price appliedPrice2 = batchMudas2.getInputProduct().getProductPrice() ; // Preço aplicado
        InputPurchaseItem item2 = new InputPurchaseItem(quantity2, appliedPrice2, batchMudas2);

        // Adicionar item à compra
        purchase.addItem(item);
        purchase.addItem(item2);

        // Salvar a compra (os itens serão salvos via cascade)
        inputPurchaseRepository.save(purchase);
    }
}
