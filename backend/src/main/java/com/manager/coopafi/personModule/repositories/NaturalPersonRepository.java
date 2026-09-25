package com.manager.coopafi.personModule.repositories;

import com.manager.coopafi.personModule.entities.NaturalPerson;
import com.manager.coopafi.infrastructure.valueObjects.Cpf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NaturalPersonRepository extends JpaRepository<NaturalPerson, Long> {
    boolean  existsByCpf(Cpf cpf);
}