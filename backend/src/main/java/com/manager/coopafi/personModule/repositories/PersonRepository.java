package com.manager.coopafi.personModule.repositories;

import com.manager.coopafi.personModule.entities.Person;
import com.manager.coopafi.personModule.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query("SELECT p FROM Person p WHERE " +
            "(:searchTerm IS NULL OR " +
            "LOWER(p.legalName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.email.addressEmail) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "TREAT(p AS NaturalPerson).cpf.cpfNumber LIKE CONCAT('%', :searchTerm, '%') OR " +
            "TREAT(p AS JuridicPerson).cnpj.cnpjNumber LIKE CONCAT('%', :searchTerm, '%'))")
    Page<Person> findAllByStatusAndSearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);
}
