package com.hvc.serverwscr02.security.repository;

import com.hvc.serverwscr02.security.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findFirstByUsername(String userName);
}
