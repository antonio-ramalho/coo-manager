package com.manager.coopafi.config;

import com.manager.coopafi.domain.entities.Farmer;
import com.manager.coopafi.domain.entities.OrganicCertificate;
import com.manager.coopafi.domain.valueObjects.Cpf;
import com.manager.coopafi.repositories.FarmerRepository;
import com.manager.coopafi.repositories.OrganicCertificateRepository;
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
public class OrganicCertificateTestConfig implements CommandLineRunner {

    @Autowired
    private OrganicCertificateRepository organicCertificateRepository;

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

        // CERTIFICADO 1: Um certificado com um farmer
        OrganicCertificate cert1 = new OrganicCertificate(
                "CERT-2024-001",
                LocalDate.now().plusYears(3),
                "Instituto de Certificação Orgânica"
        );

        // CERTIFICADO 2: Um certificado com dois farmers
        OrganicCertificate cert2 = new OrganicCertificate(
                "CERT-2024-002",
                LocalDate.now().plusYears(2),
                "Certificadora Nacional de Produtos Orgânicos"
        );

        organicCertificateRepository.save(cert1);
        organicCertificateRepository.save(cert2);

        // Linkar farmers aos certificados
        cert1.addFarmer(farmer1);
        cert2.addFarmer(farmer2);
        cert2.addFarmer(farmer3);
    }
}

