package com.manager.coopafi.repositories;

import com.manager.coopafi.domain.entities.AgriculturalProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgriculturalProductRepository extends JpaRepository<AgriculturalProduct, Long> {
}
