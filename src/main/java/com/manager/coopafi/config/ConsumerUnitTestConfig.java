package com.manager.coopafi.config;

import com.manager.coopafi.domain.entities.Agent;
import com.manager.coopafi.domain.entities.ConsumerUnit;
import com.manager.coopafi.domain.entities.JuridicPerson;
import com.manager.coopafi.domain.entities.NaturalPerson;
import com.manager.coopafi.domain.valueObjects.*;
import com.manager.coopafi.repositories.ConsumerUnitRepository;
import com.manager.coopafi.repositories.JuridicPersonRepository;
import com.manager.coopafi.repositories.NaturalPersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import java.util.Arrays;

@Configuration
@Profile("test")
@Order(2)
public class ConsumerUnitTestConfig implements CommandLineRunner {

    @Autowired
    private ConsumerUnitRepository consumerUnitRepository;

    @Autowired
    private JuridicPersonRepository juridicPersonRepository;

    @Autowired
    private NaturalPersonRepository naturalPersonRepository;

    @Override
    public void run(String... args) throws Exception {

        // BUSCA POR CNPJ
        Cnpj cnpjSearched1 = new Cnpj("11222333000100");
        JuridicPerson juridicPersonInst1 = juridicPersonRepository.findByCnpj(cnpjSearched1)
                .orElseThrow(() -> new RuntimeException("PJ não encontrada com o CNPJ informado"));

        Cnpj cnpjSearched2 = new Cnpj("44555666000199");
        JuridicPerson juridicPersonInst2 = juridicPersonRepository.findByCnpj(cnpjSearched2)
                .orElseThrow(() -> new RuntimeException("PJ não encontrada com o CNPJ informado"));

        // BUSCA POR CPF
        Cpf cpfSearched1 = new Cpf("69899167029");
        NaturalPerson naturalPersonInst1 = naturalPersonRepository.findByCpf(cpfSearched1)
                .orElseThrow(() -> new RuntimeException("PF não encontrada com o CPF informado"));

        Cpf cpfSearched2 = new Cpf("97467868065");
        NaturalPerson naturalPersonInst2 = naturalPersonRepository.findByCpf(cpfSearched2)
                .orElseThrow(() -> new RuntimeException("PF não encontrada com o CPF informado"));

        // INSTANCIAÇÃO DO AGENTE E DA UNIDADE
        Agent scholarAgent = new Agent("Diretora Geral", naturalPersonInst1);
        ConsumerUnit scholarUnit = new ConsumerUnit(
                scholarAgent,
                juridicPersonInst1.getAddress(),
                juridicPersonInst1
        );

        Agent hospitalAgent = new Agent("Gerente de Logística", naturalPersonInst2);
        ConsumerUnit hospitalUnit = new ConsumerUnit(
                hospitalAgent,
                new Address(new Cep("85015000"), "Rua Lateral de Carga", "Hospitalar", "Guarapuava", "S/N"),
                juridicPersonInst2
        );

        // SALVAR NO BANCO A UNIDADE
        consumerUnitRepository.saveAll(Arrays.asList(scholarUnit, hospitalUnit));
    }
}
