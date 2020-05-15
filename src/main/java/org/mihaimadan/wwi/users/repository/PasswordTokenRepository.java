package org.mihaimadan.wwi.users.repository;

import org.mihaimadan.wwi.users.model.PasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordTokenRepository extends JpaRepository<PasswordToken, Long> {
    Optional<PasswordToken> findByToken(String token);

    PasswordToken findByEmail(String email);
}
