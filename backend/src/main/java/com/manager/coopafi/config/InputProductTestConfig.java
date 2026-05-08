package com.manager.coopafi.config;

import com.manager.coopafi.domain.entities.InputProduct;
import com.manager.coopafi.domain.valueObjects.Ncm;
import com.manager.coopafi.domain.valueObjects.Price;
import com.manager.coopafi.enums.MeasureUnit;
import com.manager.coopafi.repositories.InputProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

@Configuration
@Profile("test")
@Order(1)
public class InputProductTestConfig implements CommandLineRunner {

    @Autowired
    private InputProductRepository inputProductRepository;

    @Override
    public void run(String... args) throws Exception {
        // INSUMO 1: Mudas de tomate
        InputProduct mudaTomate = new InputProduct(
                MeasureUnit.UNIT,
                new Ncm("06021900"),
                "Mudas de Tomate",
                new Price(BigDecimal.valueOf(2.50)),
                LocalDate.now().plusMonths(2),
                "MUD-TOM-001"
        );

        // INSUMO 2: Sementes de milho
        InputProduct sementesMilho = new InputProduct(
                MeasureUnit.KILOGRAM,
                new Ncm("10059090"),
                "Sementes de Milho Híbrido",
                new Price(BigDecimal.valueOf(85.00)),
                LocalDate.now().plusYears(1),
                "SEM-MIL-002"
        );

        // INSUMO 3: Adubo químico NPK
        InputProduct aduboNPK = new InputProduct(
                MeasureUnit.KILOGRAM,
                new Ncm("31051000"),
                "Adubo NPK 10-10-10",
                new Price(BigDecimal.valueOf(35.50)),
                LocalDate.now().plusMonths(6),
                "ADU-NPK-003"
        );

        inputProductRepository.saveAll(Arrays.asList(mudaTomate, sementesMilho, aduboNPK));
    }
}

