package com.manager.coomanager.customerModule.repositories;

import com.manager.coomanager.shared.domain.person.NaturalPerson;
import com.manager.coomanager.shared.domain.valueobjects.Cpf;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NaturalPersonRepository extends JpaRepository<NaturalPerson, Long> {
    boolean existsByCpf(Cpf cpf);
}