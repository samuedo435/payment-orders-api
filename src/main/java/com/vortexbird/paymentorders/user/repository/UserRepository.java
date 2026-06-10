package com.vortexbird.paymentorders.user.repository;

import com.vortexbird.paymentorders.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
/**
 * Acceso a datos para usuarios.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}