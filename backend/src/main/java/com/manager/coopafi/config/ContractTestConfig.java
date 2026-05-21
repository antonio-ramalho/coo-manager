package com.manager.coopafi.config;

import com.manager.coopafi.domain.entities.*;
import com.manager.coopafi.domain.valueObjects.Price;
import com.manager.coopafi.domain.valueObjects.Quantity;
import com.manager.coopafi.enums.ParticipationLimitRule;
import com.manager.coopafi.enums.ProductDeliveryRule;
import com.manager.coopafi.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Configuration
@Profile("test")
@Order(11)
public class ContractTestConfig implements CommandLineRunner {

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private InstitutionRepository institutionRepository;

    @Autowired
    private FarmerRepository farmerRepository;

    @Autowired
    private ConsumerUnitRepository consumerUnitRepository;

    @Autowired
    private AgriculturalProductRepository agriculturalProductRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        Institution institution = institutionRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Instância de Instituição requerida não encontrada."));

        // VALOR DO CONTRATO: R$ 30.000,00
        Contract contract = new Contract(LocalDate.of(2026, 12, 31),
                new Price(new BigDecimal("30000.00")), new Price(new BigDecimal("15000.00")),
                ParticipationLimitRule.PER_CPF, ProductDeliveryRule.QUANTITY_LOCKED, institution);

        List<AgriculturalProduct> baseProducts = agriculturalProductRepository.findAll();
        if(baseProducts.size() < 2) return; // Segurança

        // 1. INJETANDO PRODUTOS NO EDITAL
        // Prod 1: Tomate a R$ 10. Max Global: 1500 und = R$ 15.000
        contract.addContractedProduct(baseProducts.get(0), new Price(new BigDecimal("10.00")),
                "Tomate Edital", new Quantity(new BigDecimal("1500.00")));

        // Prod 2: Alface a R$ 20. Max Global: 750 und = R$ 15.000
        contract.addContractedProduct(baseProducts.get(1), new Price(new BigDecimal("20.00")),
                "Alface Edital", new Quantity(new BigDecimal("750.00")));


        // 2. INJETANDO AGRICULTORES COM A MATEMÁTICA EXATA
        List<Farmer> farmers = farmerRepository.findAll();
        farmers.stream().limit(3).forEach(farmer -> {

            // Cada um dos 3 agricultores ganha exatos R$ 10.000 (3 * 10k = 30k do Contrato)
            Price farmerQuota = new Price(new BigDecimal("10000.00"));
            FarmerContract participation = contract.addFarmerParticipation(farmer, farmerQuota);

            // Produto 1: 500 tomates * R$ 10 = R$ 5.000
            participation.addProductQuota(contract.getProducts().get(0), new Quantity(new BigDecimal("500.00")));

            // Produto 2: 250 alfaces * R$ 20 = R$ 5.000
            // Soma do agricultor = R$ 10.000 -> Bateu!
            participation.addProductQuota(contract.getProducts().get(1), new Quantity(new BigDecimal("250.00")));
        });

        // 3. INJETANDO CONSUMIDORES
        List<ConsumerUnit> consumerUnits = consumerUnitRepository.findAll();
        consumerUnits.stream().limit(2).forEach(contract::addConsumerUnit);

        // 4. A MAGIA ACONTECE E TUDO É SALVO (Cascade Mágico)

        contract.validateContractIntegrity(); // Chama a validação para garantir que o Mock está perfeito
        contractRepository.save(contract);
    }
}

