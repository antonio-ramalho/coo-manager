package com.manager.coopafi.config;

import com.manager.coopafi.domain.entities.Farmer;
import com.manager.coopafi.domain.entities.NaturalPerson;
import com.manager.coopafi.domain.valueObjects.*;
import com.manager.coopafi.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import java.util.Arrays;

@Configuration
@Profile("test")
@Order(2)
public class FarmerTestConfig implements CommandLineRunner {

    @Autowired
    private FarmerRepository farmerRepository;

    @Autowired
    private NaturalPersonRepository naturalPersonRepository;

    @Override
    public void run(String... args) throws Exception {

        // BUSCA POR CPF
        Cpf cpfSearched1 = new Cpf("20560239033");
        NaturalPerson naturalPersonInst1 = naturalPersonRepository.findByCpf(cpfSearched1)
                .orElseThrow(() -> new RuntimeException("PF não encontrada com o CPF informado"));

        Cpf cpfSearched2 = new Cpf("75953380003");
        NaturalPerson naturalPersonInst2 = naturalPersonRepository.findByCpf(cpfSearched2)
                .orElseThrow(() -> new RuntimeException("PF não encontrada com o CPF informado"));

        Cpf cpfSearched3 = new Cpf("25707897046");
        NaturalPerson naturalPersonInst3 = naturalPersonRepository.findByCpf(cpfSearched3)
                .orElseThrow(() -> new RuntimeException("PF não encontrada com o CPF informado"));

        Cpf cpfSearched4 = new Cpf("62377858090");
        NaturalPerson naturalPersonInst4 = naturalPersonRepository.findByCpf(cpfSearched4)
                .orElseThrow(() -> new RuntimeException("PF não encontrada com o CPF informado"));

        Farmer f1 = new Farmer(naturalPersonInst1);
        Farmer f2 = new Farmer(naturalPersonInst2);
        Farmer f3 = new Farmer(naturalPersonInst3);
        Farmer f4 = new Farmer(naturalPersonInst4);

        farmerRepository.saveAll(Arrays.asList(f1, f2, f3, f4));
    }
}
