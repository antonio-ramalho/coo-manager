package com.manager.coopafi.config;

import com.manager.coopafi.domain.entities.Caf;
import com.manager.coopafi.domain.entities.Farmer;
import com.manager.coopafi.domain.entities.JuridicPerson;
import com.manager.coopafi.domain.entities.NaturalPerson;
import com.manager.coopafi.domain.valueObjects.*;
import com.manager.coopafi.enums.SettlementType;
import com.manager.coopafi.repositories.CafRepository;
import com.manager.coopafi.repositories.FarmerRepository;
import com.manager.coopafi.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;
import java.util.Arrays;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

    @Autowired
    private FarmerRepository farmerRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private CafRepository cafRepository;

    @Override
    public void run(String... args) throws Exception {
        NaturalPerson p1 = new NaturalPerson(
                new Address(new Cep("80010000"), "Rua das Flores", "Centro", "Curitiba", "10"),
                new Email("joao@agro.com"),
                new Phone("41999998888"),
                "João do Arroz",
                new Cpf("20560239033"),
                new BirthDate(LocalDate.of(1985, 5, 20))
        );

        NaturalPerson p2 = new NaturalPerson(
                new Address(new Cep("87010000"), "Av. Colombo", "Zona 7", "Maringá", "1200"),
                new Email("ricardo.soja@maringa.com"),
                new Phone("44988881111"),
                "Ricardo Oliveira",
                new Cpf("75953380003"), // CPF Válido (1º dígito: 1)
                new BirthDate(LocalDate.of(1990, 3, 15))
        );

        NaturalPerson p3 = new NaturalPerson(
                new Address(new Cep("84010000"), "Rua Balduíno Taques", "Estrela", "Ponta Grossa", "45"),
                new Email("ana.beatriz@camposgerais.com"),
                new Phone("42977772222"),
                "Ana Beatriz",
                new Cpf("25707897046"), // CPF Válido (1º dígito: 3)
                new BirthDate(LocalDate.of(1982, 8, 10))
        );

        NaturalPerson p4 = new NaturalPerson(
                new Address(new Cep("85010000"), "Rua XV de Novembro", "Centro", "Guarapuava", "99"),
                new Email("benedito.silva@guarapuava.com"),
                new Phone("42966663333"),
                "Benedito Silva",
                new Cpf("62377858090"), // CPF Válido (1º dígito: 5)
                new BirthDate(LocalDate.of(1975, 12, 01))
        );

        JuridicPerson p5 = new JuridicPerson(
                new Address(new Cep("85801000"), "Av. Brasil", "Centro", "Cascavel", "500"),
                new Email("contato@sojaforte.com.br"),
                new Phone("4533221100"),
                new BirthDate(LocalDate.of(2000, 10, 10)),
                new Cnpj("12345678000199"),
                "Soja Forte Logística LTDA",
                "Soja Forte"
        );
        personRepository.saveAll(Arrays.asList(p1, p2, p3, p4, p5));

        Farmer f1 = new Farmer(p1);
        Farmer f2 = new Farmer(p2);
        Farmer f3 = new Farmer(p3);
        Farmer f4 = new Farmer(p4);

        Caf c1 = new Caf(new CafNumber("PR-12345-6789"),LocalDate.now().plusYears(2),SettlementType.REGULAR);
        Caf c2 = new Caf(new CafNumber("PR-14345-6789"),LocalDate.now().plusYears(3),SettlementType.REGULAR);

        f1.linkCaf(c1);
        f2.linkCaf(c2);
        f3.linkCaf(c1);

        cafRepository.saveAll(Arrays.asList(c1, c2));
        farmerRepository.saveAll(Arrays.asList(f1, f2, f3, f4));
    }
}
