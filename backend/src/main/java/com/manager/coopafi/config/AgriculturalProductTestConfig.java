package com.manager.coopafi.config;

import com.manager.coopafi.domain.entities.AgriculturalProduct;
import com.manager.coopafi.domain.valueObjects.Ncm;
import com.manager.coopafi.domain.valueObjects.Price;
import com.manager.coopafi.enums.CultivationType;
import com.manager.coopafi.enums.MeasureUnit;
import com.manager.coopafi.enums.ProductGroup;
import com.manager.coopafi.repositories.AgriculturalProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import java.math.BigDecimal;
import java.util.Arrays;

@Configuration
@Profile("test")
@Order(1)
public class AgriculturalProductTestConfig implements CommandLineRunner {

    @Autowired
    private AgriculturalProductRepository agriculturalProductRepository;

    @Override
    public void run(String... args) throws Exception {
        // PRODUTO 1: Alface
        AgriculturalProduct alface = new AgriculturalProduct(
                MeasureUnit.KILOGRAM,
                new Ncm("07055100"),
                "Alface Crespa",
                new Price(BigDecimal.valueOf(4.50)),
                CultivationType.ORGANIC,
                ProductGroup.VEGETABLES
        );

        // PRODUTO 2: Tomate
        AgriculturalProduct tomate = new AgriculturalProduct(
                MeasureUnit.KILOGRAM,
                new Ncm("07020000"),
                "Tomate Caqui",
                new Price(BigDecimal.valueOf(5.80)),
                CultivationType.CONVENTIONAL,
                ProductGroup.VEGETABLES
        );

        // PRODUTO 3: Morango
        AgriculturalProduct morango = new AgriculturalProduct(
                MeasureUnit.KILOGRAM,
                new Ncm("08081000"),
                "Morango Fresco",
                new Price(BigDecimal.valueOf(12.00)),
                CultivationType.ORGANIC,
                ProductGroup.FRUITS
        );

        // PRODUTO 4: Maçã
        AgriculturalProduct maca = new AgriculturalProduct(
                MeasureUnit.KILOGRAM,
                new Ncm("08081000"),
                "Maçã Gala",
                new Price(BigDecimal.valueOf(6.50)),
                CultivationType.CONVENTIONAL,
                ProductGroup.FRUITS
        );

        // PRODUTO 5: Batata
        AgriculturalProduct batata = new AgriculturalProduct(
                MeasureUnit.KILOGRAM,
                new Ncm("07019090"),
                "Batata Baroa",
                new Price(BigDecimal.valueOf(3.20)),
                CultivationType.CONVENTIONAL,
                ProductGroup.VEGETABLES
        );

        // PRODUTO 6: Beterraba
        AgriculturalProduct beterraba = new AgriculturalProduct(
                MeasureUnit.KILOGRAM,
                new Ncm("07061100"),
                "Beterraba Vermelha",
                new Price(BigDecimal.valueOf(5.00)),
                CultivationType.ORGANIC,
                ProductGroup.VEGETABLES
        );

        // PRODUTO 7: Banana
        AgriculturalProduct banana = new AgriculturalProduct(
                MeasureUnit.KILOGRAM,
                new Ncm("08039000"),
                "Banana Nanica",
                new Price(BigDecimal.valueOf(4.00)),
                CultivationType.ORGANIC,
                ProductGroup.FRUITS
        );

        // PRODUTO 8: Cenoura
        AgriculturalProduct cenoura = new AgriculturalProduct(
                MeasureUnit.KILOGRAM,
                new Ncm("07061500"),
                "Cenoura Laranja",
                new Price(BigDecimal.valueOf(3.50)),
                CultivationType.CONVENTIONAL,
                ProductGroup.VEGETABLES
        );

        // PRODUTO 9: Laranja
        AgriculturalProduct laranja = new AgriculturalProduct(
                MeasureUnit.KILOGRAM,
                new Ncm("08051000"),
                "Laranja Pêra",
                new Price(BigDecimal.valueOf(5.20)),
                CultivationType.CONVENTIONAL,
                ProductGroup.FRUITS
        );

        // PRODUTO 10: Repolho
        AgriculturalProduct repolho = new AgriculturalProduct(
                MeasureUnit.KILOGRAM,
                new Ncm("07051100"),
                "Repolho Verde",
                new Price(BigDecimal.valueOf(2.80)),
                CultivationType.ORGANIC,
                ProductGroup.VEGETABLES
        );

        agriculturalProductRepository.saveAll(Arrays.asList(
                alface, tomate, morango, maca, batata,
                beterraba, banana, cenoura, laranja, repolho
        ));
    }
}

