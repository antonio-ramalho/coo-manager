package com.manager.coomanager.iamModule.repositories;

import com.manager.coomanager.iamModule.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    List<Role> findByCooperativeId(Long cooperativeId);

    List<Role> findBySystemRoleTrue();

    @Query("SELECT r FROM Role r WHERE r.systemRole = true OR r.cooperative.id = :cooperativeId")
    List<Role> findAllAvailableForCooperative(@Param("cooperativeId") Long cooperativeId);

    boolean existsByNameAndCooperativeId(String name, Long cooperativeId);

    Optional<Role> findByNameAndCooperativeId(String name, Long cooperativeId);
}
