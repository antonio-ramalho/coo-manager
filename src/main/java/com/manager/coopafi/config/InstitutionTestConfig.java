package com.manager.coopafi.config;

import com.manager.coopafi.domain.entities.Institution;
import com.manager.coopafi.domain.entities.JuridicPerson;
import com.manager.coopafi.domain.valueObjects.Cnpj;
import com.manager.coopafi.enums.InstitutionSphere;
import com.manager.coopafi.repositories.InstitutionRepository;
import com.manager.coopafi.repositories.JuridicPersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

@Configuration
@Profile("test")
@Order(2)
public class InstitutionTestConfig implements CommandLineRunner {

    @Autowired
    private InstitutionRepository institutionRepository;

    @Autowired
    private JuridicPersonRepository juridicPersonRepository;

    @Override
    public void run(String... args) throws Exception {

        // BUSCA JURIDIC PERSON POR CNPJ
        Cnpj cnpjSearched = new Cnpj("12345678000199");
        JuridicPerson juridicPerson = juridicPersonRepository.findByCnpj(cnpjSearched)
                .orElseThrow(() -> new RuntimeException("JuridicPerson não encontrada com o CNPJ informado"));

        // INSTITUTION: Uma instituição com a JuridicPerson encontrada
        Institution institution = new Institution(InstitutionSphere.PUBLIC, juridicPerson);

        institutionRepository.save(institution);
    }
}

