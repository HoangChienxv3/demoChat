package com.hvc.serverwscr02.dto;

import com.hvc.serverwscr02.security.model.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    Long id;
    String username;

    public UserResponse(Users users){
        this.id = users.getId();
        this.username = users.getUsername();
    }
}
