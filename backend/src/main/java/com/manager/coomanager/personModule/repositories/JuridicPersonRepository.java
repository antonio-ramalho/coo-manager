package com.manager.coomanager.personModule.repositories;

import com.manager.coomanager.personModule.entities.JuridicPerson;
import com.manager.coomanager.shared.domain.valueobjects.Cnpj;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JuridicPersonRepository extends JpaRepository<JuridicPerson, Long> {
    boolean existsByCnpj(Cnpj cnpj);
}
