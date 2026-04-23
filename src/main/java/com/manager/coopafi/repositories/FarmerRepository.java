package com.manager.coopafi.repositories;

import com.manager.coopafi.domain.entities.Farmer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FarmerRepository extends JpaRepository<Farmer, Long> {
}
