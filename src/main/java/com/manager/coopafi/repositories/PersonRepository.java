package com.manager.coopafi.repositories;

import com.manager.coopafi.domain.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
