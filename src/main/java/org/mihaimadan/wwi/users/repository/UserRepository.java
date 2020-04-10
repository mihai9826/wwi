package org.mihaimadan.wwi.users.repository;

import org.mihaimadan.wwi.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAddress(String emailAddress);

    Boolean existsByEmailAddress(String emailAddress);
}
