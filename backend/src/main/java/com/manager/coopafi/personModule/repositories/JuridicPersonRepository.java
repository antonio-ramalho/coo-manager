package com.manager.coopafi.personModule.repositories;

import com.manager.coopafi.personModule.entities.JuridicPerson;
import com.manager.coopafi.infrastructure.valueObjects.Cnpj;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JuridicPersonRepository extends JpaRepository<JuridicPerson, Long> {
    boolean existsByCnpj(Cnpj cnpj);
}
