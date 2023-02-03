package com.hvc.serverwscr02.security.service.service;

import com.hvc.serverwscr02.security.model.Users;

public interface UserService {
    Users findUserByUserName(String userName);

    Users findUserByUserId(Long userId);
}
