package com.manager.coopafi.config;

import com.manager.coopafi.domain.entities.Caf;
import com.manager.coopafi.domain.entities.Farmer;
import com.manager.coopafi.domain.valueObjects.CafNumber;
import com.manager.coopafi.domain.valueObjects.Cpf;
import com.manager.coopafi.enums.SettlementType;
import com.manager.coopafi.repositories.CafRepository;
import com.manager.coopafi.repositories.FarmerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import java.time.LocalDate;

@Configuration
@Profile("test")
@Order(3)
public class CafTestConfig implements CommandLineRunner {

    @Autowired
    private CafRepository cafRepository;

    @Autowired
    private FarmerRepository farmerRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        // BUSCA FARMERS POR CPF
        Cpf cpfSearched1 = new Cpf("20560239033");
        Farmer farmer1 = farmerRepository.findByPerson_Cpf(cpfSearched1)
                .orElseThrow(() -> new RuntimeException("Farmer não encontrado com o CPF informado"));

        Cpf cpfSearched2 = new Cpf("75953380003");
        Farmer farmer2 = farmerRepository.findByPerson_Cpf(cpfSearched2)
                .orElseThrow(() -> new RuntimeException("Farmer não encontrado com o CPF informado"));

        Cpf cpfSearched3 = new Cpf("25707897046");
        Farmer farmer3 = farmerRepository.findByPerson_Cpf(cpfSearched3)
                .orElseThrow(() -> new RuntimeException("Farmer não encontrado com o CPF informado"));

        Cpf cpfSearched4 = new Cpf("62377858090");
        Farmer farmer4 = farmerRepository.findByPerson_Cpf(cpfSearched4)
                .orElseThrow(() -> new RuntimeException("Farmer não encontrado com o CPF informado"));

        // CAF 1: Uma CAF com um farmer
        Caf caf1 = new Caf(new CafNumber("PR-12345-6789"), LocalDate.now().plusYears(2), SettlementType.REGULAR);

        // CAF 2: Uma CAF com mais de um farmer (dois farmers)
        Caf caf2 = new Caf(new CafNumber("PR-14345-6789"), LocalDate.now().plusYears(3), SettlementType.REGULAR);

        // CAF 3: Uma CAF com um farmer
        Caf caf3 = new Caf(new CafNumber("PR-15345-6789"), LocalDate.now().plusYears(1), SettlementType.REGULAR);

        cafRepository.save(caf1);
        cafRepository.save(caf2);
        cafRepository.save(caf3);

        caf1.addFarmer(farmer1);
        caf2.addFarmer(farmer2);
        caf2.addFarmer(farmer3);
        caf3.addFarmer(farmer4);
    }
}
