package com.hvc.serverwscr02.socket.dto.response;

import com.hvc.serverwscr02.security.model.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SenderResponse {
    Long id;
    String userName;

    public SenderResponse(Users user) {
        this.id = user.getId();
        this.userName = user.getUsername();
    }
}
