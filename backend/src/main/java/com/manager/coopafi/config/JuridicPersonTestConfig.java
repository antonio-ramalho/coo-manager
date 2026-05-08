package com.manager.coopafi.config;

import com.manager.coopafi.domain.entities.JuridicPerson;
import com.manager.coopafi.domain.valueObjects.*;
import com.manager.coopafi.repositories.JuridicPersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import java.time.LocalDate;
import java.util.Arrays;

@Configuration
@Profile("test")
@Order(1)
public class JuridicPersonTestConfig implements CommandLineRunner {

    @Autowired
    private JuridicPersonRepository juridicPersonRepository;

    @Override
    public void run(String... args) throws Exception {
        JuridicPerson escolaPJ = new JuridicPerson(
                new Address(new Cep("85810000"), "Rua das Escolas", "Centro", "Cascavel", "100"),
                new Email("contato@escolamaria.edu.br"),
                new Phone("4532221111"),
                new BirthDate(LocalDate.of(1995, 5, 20)),
                new Cnpj("11222333000100"),
                "Escola Municipal Maria da Luz",
                "Escola Maria da Luz"
        );

        JuridicPerson hospitalPJ = new JuridicPerson(
                new Address(new Cep("85015000"), "Av. da Saúde", "Hospitalar", "Guarapuava", "500"),
                new Email("compras@hospitalregional.org"),
                new Phone("4236210000"),
                new BirthDate(LocalDate.of(1970, 1, 1)),
                new Cnpj("44555666000199"),
                "Hospital Regional do Centro Oeste",
                "Hospital Regional"
        );

        JuridicPerson i1 = new JuridicPerson(
                new Address(new Cep("85801000"), "Av. Brasil", "Centro", "Cascavel", "500"),
                new Email("contato@sojaforte.com.br"),
                new Phone("4533221100"),
                new BirthDate(LocalDate.of(2000, 10, 10)),
                new Cnpj("12345678000199"),
                "Soja Forte Logística LTDA",
                "Soja Forte"
        );
        juridicPersonRepository.saveAll(Arrays.asList(escolaPJ, hospitalPJ, i1));
    }
}
