package com.hvc.serverwscr02.security.repository;

import com.hvc.serverwscr02.security.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findFirstByUsername(String userName);

    List<Users> findAllByIdIn(Set<Long> ids);

}
