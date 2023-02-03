package com.hvc.serverwscr02.security.service.service;

import com.hvc.serverwscr02.security.model.Users;

import java.util.List;
import java.util.Set;

public interface UserService {
    Users findUserByUserName(String userName);

    Users findUserByUserId(Long userId);

    List<Users> findAllByIdIn(Set<Long> ids);
}
