package com.hvc.serverwscr02.security.service.impl;

import com.hvc.serverwscr02.security.model.Users;
import com.hvc.serverwscr02.security.repository.UserRepository;
import com.hvc.serverwscr02.security.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public Users findUserByUserName(String userName) {
        return userRepository.findFirstByUsername(userName).orElse(null);
    }

    @Override
    public Users findUserByUserId(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public List<Users> findAllByIdIn(Set<Long> ids) {
        return userRepository.findAllByIdIn(ids);
    }
}
