package com.hvc.serverwscr02.security.controller;

import com.hvc.serverwscr02.security.model.Users;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ResponseStatusException;

public interface BaserController {

    default Users getUser() {
        Object userObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(userObj instanceof UserDetails)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ACCESS_DENIED");
        }
        UserDetails userDetails = (UserDetails) userObj;

        Users users = new Users();
        users.setUsername(userDetails.getUsername());
        users.setPassword(userDetails.getPassword());
        return users;
    }
}
