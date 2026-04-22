package com.manager.coopafi.config;

import com.manager.coopafi.domain.entities.Farmer;
import com.manager.coopafi.domain.valueObjects.*;
import com.manager.coopafi.repositories.FarmerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

    @Autowired
    private FarmerRepository farmerRepository;

    @Override
    public void run(String... args) throws Exception {

        Farmer farmer1 = new Farmer(
                null,
                new Cpf("24567328043"),
                new Address( new Cep("80010000"),
                        "Rua das Flores", "Centro", "Curitiba", "10"),
                new Email("joao@agro.com"),
                "João do Arroz",
                new Phone("41999998888")
        );

        Farmer farmer2 = new Farmer(
                null,
                new Cpf("50180723006"),
                new Address( new Cep("85801000"),
                        "Av. Brasil", "Cascavel Velho", "Cascavel", "500"),
                new Email("maria@soja.com.br"),
                "Maria da Soja",
                new Phone("45988887777")
        );

        Farmer farmer3 = new Farmer(
                null,
                new Cpf("28576584000"),
                new Address( new Cep("86010000"),
                        "Rua Sergipe", "Vila Nova", "Londrina", "123-A"),
                new Email("lucas@milhotech.io"),
                "Lucas do Milho",
                new Phone("43977776666")
        );

        farmerRepository.saveAll(Arrays.asList(farmer1,farmer2,farmer3));
    }
}
