package com.manager.coomanager.personModule.repositories;

import com.manager.coomanager.personModule.entities.NaturalPerson;
import com.manager.coomanager.shared.domain.valueobjects.Cpf;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NaturalPersonRepository extends JpaRepository<NaturalPerson, Long> {
    boolean existsByCpf(Cpf cpf);
}