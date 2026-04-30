package com.manager.coopafi.repositories;

import com.manager.coopafi.domain.entities.InputProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InputProductRepository extends JpaRepository<InputProduct, Long> {
    Optional<InputProduct> findByProductCode(String productCode);
}