package com.manager.coopafi.repositories;

import com.manager.coopafi.domain.entities.*;
import com.manager.coopafi.domain.valueObjects.*;
import com.manager.coopafi.enums.MeasureUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class InputPurchaseCascadeTest {

    @Autowired
    private InputPurchaseRepository inputPurchaseRepository;
    @Autowired
    private FarmerRepository farmerRepository;
    @Autowired
    private InputBatchRepository inputBatchRepository;
    @Autowired
    private NaturalPersonRepository naturalPersonRepository;
    @Autowired
    private InputProductRepository inputProductRepository;

    @Test
    @DisplayName("Deve salvar uma Compra e seus Itens automaticamente via Cascade")
    void shouldSavePurchaseAndItemsViaCascade() {
        Farmer farmer = farmerRepository.save(createValidFarmer());
        InputBatch batch = inputBatchRepository.save(createValidInputBatch());

        InputPurchase purchase = new InputPurchase(farmer);

        Quantity buyQuantity = new Quantity(new BigDecimal("10.000"));
        Price appliedPrice = new Price(new BigDecimal("5.00"));

        InputPurchaseItem item = new InputPurchaseItem(buyQuantity, appliedPrice, batch);
        purchase.addItem(item);

        InputPurchase savedPurchase = inputPurchaseRepository.save(purchase);

        assertThat(savedPurchase.getId()).isNotNull();
        assertThat(savedPurchase.getPurchaseItems()).hasSize(1);
        assertThat(savedPurchase.getPurchaseItems().get(0).getId()).isNotNull();
        assertThat(savedPurchase.getTotalValue()).isEqualTo(new Price(new BigDecimal("50.00")));
    }
    private Farmer createValidFarmer() {
        NaturalPerson person = new NaturalPerson(
                new Address(new Cep("85810000"), "Rua A", "Centro", "Cascavel", "10"),
                new Email("teste@teste.com"),
                new Phone("45999999999"),
                "Agricultor Teste",
                new Cpf("73672281026"),
                new BirthDate(LocalDate.of(1990, 1, 1))
        );
        naturalPersonRepository.save(person);
        return new Farmer(person);
    }

    private InputBatch createValidInputBatch() {
        InputProduct product = new InputProduct(
                MeasureUnit.KILOGRAM,
                new Ncm("31051000"),
                "Adubo NPK 10-10-10",
                new Price(BigDecimal.valueOf(35.50)),
                LocalDate.now().plusMonths(6),
                "ADU-NPK-003"
        );
        inputProductRepository.save(product);

        return new InputBatch(
                new Quantity(new BigDecimal("100.000")),
                product,
                new Quantity(new BigDecimal("100.000"))
        );
    }
}