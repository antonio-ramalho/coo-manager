package com.manager.coomanager.personModule.repositories;

import com.manager.coomanager.personModule.entities.JuridicPerson;
import com.manager.coomanager.infrastructure.valueObjects.Cnpj;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JuridicPersonRepository extends JpaRepository<JuridicPerson, Long> {
    boolean existsByCnpj(Cnpj cnpj);
}
