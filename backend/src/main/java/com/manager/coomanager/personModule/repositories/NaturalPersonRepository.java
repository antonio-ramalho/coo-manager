package com.manager.coomanager.personModule.repositories;

import com.manager.coomanager.personModule.entities.NaturalPerson;
import com.manager.coomanager.infrastructure.valueObjects.Cpf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NaturalPersonRepository extends JpaRepository<NaturalPerson, Long> {
    boolean  existsByCpf(Cpf cpf);
}