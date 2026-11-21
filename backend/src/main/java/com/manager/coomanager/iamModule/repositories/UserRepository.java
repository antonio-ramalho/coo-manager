package com.manager.coomanager.iamModule.repositories;

import com.manager.coomanager.shared.domain.valueobjects.Email;
import com.manager.coomanager.iamModule.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

        Optional<User> findByEmail(Email email);

        @Query("SELECT u FROM User u JOIN FETCH u.cooperative WHERE u.email = :email")
        Optional<User> findByEmailWithCooperative(@Param("email") Email email);

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
