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
        // Buscar uma instituição existente
        Institution institution = institutionRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Nenhuma instituição encontrada"));

        // Criar o contrato com valores e regras
        LocalDate finalDate = LocalDate.now().plusYears(1);
        Price totalValue = new Price(BigDecimal.valueOf(50000.00)); // R$ 50.000
        Price globalLimit = new Price(BigDecimal.valueOf(5000.00)); // R$ 5.000 por agricultor

        Contract contract = new Contract(
                finalDate,
                totalValue,
                globalLimit,
                ParticipationLimitRule.PER_CPF,
                ProductDeliveryRule.VALUE_ONLY,
                institution
        );

        // Buscar farmers existentes e adicioná-los ao contrato
        List<Farmer> farmers = farmerRepository.findAll();
        farmers.stream().limit(2).forEach(farmer -> {
            Price farmerQuota = new Price(BigDecimal.valueOf(5000.00)); // R$ 5.000 por agricultor
            contract.addFarmerParticipation(farmer, farmerQuota);
        });

        // Buscar consumer units existentes e adicioná-las ao contrato
        List<ConsumerUnit> consumerUnits = consumerUnitRepository.findAll();
        consumerUnits.stream().limit(2).forEach(contract::addConsumerUnit);

        // Buscar agricultural products e adicioná-los como contracted products
        List<AgriculturalProduct> products = agriculturalProductRepository.findAll();
        products.stream().limit(3).forEach(product -> {
            Price fixedPrice = new Price(BigDecimal.valueOf(10.00)); // Preço fixo de R$ 10
            String contractedName = "Contratação de " + product.getProductName();
            contract.addContractedProduct(product, fixedPrice, contractedName);
        });

        // Adicionar quotas de produtos para cada agricultor
        for (FarmerContract farmerContract : contract.getFarmerContracts()) {
            for (ContractedProduct contractedProduct : contract.getProducts()) {
                Quantity maxQuantity = new Quantity(BigDecimal.valueOf(100.00)); // Máximo de 100 unidades
                farmerContract.addProductQuota(contractedProduct, maxQuantity);
            }
        }

        // Salvar o contrato (todas as entidades relacionadas serão salvas via cascade)
        contractRepository.save(contract);
    }
}

