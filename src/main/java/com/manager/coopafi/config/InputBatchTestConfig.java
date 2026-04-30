package com.manager.coopafi.config;

import com.manager.coopafi.domain.entities.InputBatch;
import com.manager.coopafi.domain.entities.InputProduct;
import com.manager.coopafi.domain.valueObjects.Quantity;
import com.manager.coopafi.repositories.InputBatchRepository;
import com.manager.coopafi.repositories.InputProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import java.math.BigDecimal;
import java.util.Arrays;

@Configuration
@Profile("test")
@Order(9)
public class InputBatchTestConfig implements CommandLineRunner {

    @Autowired
    private InputBatchRepository inputBatchRepository;

    @Autowired
    private InputProductRepository inputProductRepository;

    @Override
    public void run(String... args) throws Exception {
        // BUSCAR OS INPUT PRODUCTS CRIADOS NO InputProductTestConfig
        InputProduct mudaTomate = inputProductRepository.findByProductCode("MUD-TOM-001")
                .orElseThrow(() -> new RuntimeException("InputProduct MUD-TOM-001 não encontrado"));

        InputProduct sementesMilho = inputProductRepository.findByProductCode("SEM-MIL-002")
                .orElseThrow(() -> new RuntimeException("InputProduct SEM-MIL-002 não encontrado"));

        InputProduct aduboNPK = inputProductRepository.findByProductCode("ADU-NPK-003")
                .orElseThrow(() -> new RuntimeException("InputProduct ADU-NPK-003 não encontrado"));

        // CRIAR BATCHES PARA CADA INPUT PRODUCT
        InputBatch batchMudas = new InputBatch(
                new Quantity(BigDecimal.valueOf(1000)), // Quantidade atual
                mudaTomate,
                new Quantity(BigDecimal.valueOf(1000))  // Quantidade original
        );

        InputBatch batchSementes = new InputBatch(
                new Quantity(BigDecimal.valueOf(500)), // Quantidade atual
                sementesMilho,
                new Quantity(BigDecimal.valueOf(500))  // Quantidade original
        );

        InputBatch batchAdubo = new InputBatch(
                new Quantity(BigDecimal.valueOf(200)), // Quantidade atual
                aduboNPK,
                new Quantity(BigDecimal.valueOf(200))  // Quantidade original
        );

        inputBatchRepository.saveAll(Arrays.asList(batchMudas, batchSementes, batchAdubo));
    }
}

