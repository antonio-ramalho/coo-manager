package com.manager.coomanager.userModule.repositories;

import com.manager.coomanager.infrastructure.valueObjects.Email;
import com.manager.coomanager.userModule.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

        Optional<User> findByEmail(Email email);

        boolean existsByEmail(Email email);

        Page<User> findAllByCooperativeId(Long cooperativeId, Pageable pageable);

        Optional<User> findByIdAndCooperativeId(Long id, Long cooperativeId);

        @Query("SELECT u FROM User u WHERE u.cooperative.id = :cooperativeId AND " +
                        "(:searchTerm IS NULL OR :searchTerm = '' OR " +
                        "LOWER(u.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
                        "LOWER(u.email.addressEmail) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
        Page<User> searchByCooperativeAndTerm(@Param("cooperativeId") Long cooperativeId,
                        @Param("searchTerm") String searchTerm,
                        Pageable pageable);
}
