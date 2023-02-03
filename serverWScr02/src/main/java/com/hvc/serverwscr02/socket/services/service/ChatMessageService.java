package com.hvc.serverwscr02.socket.services.service;

import com.hvc.serverwscr02.security.model.Users;
import com.hvc.serverwscr02.socket.dto.request.RequestMessage;

public interface ChatMessageService {
    void chatRoom(RequestMessage message, Users user);

}
