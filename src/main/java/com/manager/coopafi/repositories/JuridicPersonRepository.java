package com.manager.coopafi.repositories;

import com.manager.coopafi.domain.entities.JuridicPerson;
import com.manager.coopafi.domain.valueObjects.Cnpj;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JuridicPersonRepository extends JpaRepository<JuridicPerson, Long> {
    Optional<JuridicPerson> findByCnpj(Cnpj cnpj);
}
