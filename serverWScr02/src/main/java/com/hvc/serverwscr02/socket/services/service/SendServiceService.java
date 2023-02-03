package com.hvc.serverwscr02.socket.services.service;

import com.hvc.serverwscr02.security.model.Users;
import com.hvc.serverwscr02.socket.model.Room;
import com.hvc.serverwscr02.socket.server.WebSocketServer;

public interface SendServiceService {

    void sendMessageToUser(String message , Users user) ;

    void sendMessageToUserInRoom(String message, Room room)  ;

    boolean checkForExistenceSerVer(Long key);

    boolean checkForExistenceRoom(String key);

    boolean checkUserExistenceRoom(String roomId, Long idUser);

    WebSocketServer getSerVer(Long key);

    Room getRoom(String key);
}
