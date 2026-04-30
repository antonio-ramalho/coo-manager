package com.manager.coopafi.repositories;

import com.manager.coopafi.domain.entities.JuridicPerson;
import com.manager.coopafi.domain.entities.NaturalPerson;
import com.manager.coopafi.domain.valueObjects.Cnpj;
import com.manager.coopafi.domain.valueObjects.Cpf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NaturalPersonRepository extends JpaRepository<NaturalPerson, Long> {
    Optional<NaturalPerson> findByCpf(Cpf cpf);
}