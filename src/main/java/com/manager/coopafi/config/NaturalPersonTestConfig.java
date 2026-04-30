package com.manager.coopafi.config;

import com.manager.coopafi.domain.entities.NaturalPerson;
import com.manager.coopafi.domain.valueObjects.*;
import com.manager.coopafi.repositories.NaturalPersonRepository;
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
public class NaturalPersonTestConfig implements CommandLineRunner {

    @Autowired
    private NaturalPersonRepository naturalPersonRepository;

    @Override
    public void run(String... args) throws Exception {
        NaturalPerson nT1 = new NaturalPerson(
                new Address(new Cep("85810000"), "Avenida Brasil", "Centro", "Cascavel", "1500"),
                new Email("carlos.agronomo@coopafi.com"),
                new Phone("45999998888"),
                "Carlos Eduardo Silva",
                new Cpf("69899167029"),
                new BirthDate(LocalDate.of(1988, 8, 15))
        );

        NaturalPerson nT2 = new NaturalPerson(
                new Address(new Cep("85195000"), "Avenida Frederico de Oliveira", "Centro", "Curitiba", "1250"),
                new Email("anaBarbosa.agronomo@coopafi.com"),
                new Phone("42988543423"),
                "Ana Barbosa Andrade",
                new Cpf("97467868065"),
                new BirthDate(LocalDate.of(1990, 10, 1))
        );

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
                new Cpf("75953380003"),
                new BirthDate(LocalDate.of(1990, 3, 15))
        );

        NaturalPerson p3 = new NaturalPerson(
                new Address(new Cep("84010000"), "Rua Balduíno Taques", "Estrela", "Ponta Grossa", "45"),
                new Email("ana.beatriz@camposgerais.com"),
                new Phone("42977772222"),
                "Ana Beatriz",
                new Cpf("25707897046"),
                new BirthDate(LocalDate.of(1982, 8, 10))
        );

        NaturalPerson p4 = new NaturalPerson(
                new Address(new Cep("85010000"), "Rua XV de Novembro", "Centro", "Guarapuava", "99"),
                new Email("benedito.silva@guarapuava.com"),
                new Phone("42966663333"),
                "Benedito Silva",
                new Cpf("62377858090"),
                new BirthDate(LocalDate.of(1975, 12, 01))
        );

        naturalPersonRepository.saveAll(Arrays.asList(nT1, nT2, p1, p2, p3, p4));
    }
}
