package com.hvc.serverwscr02.socket.services.impl;

import com.hvc.serverwscr02.security.model.Users;
import com.hvc.serverwscr02.socket.model.Room;
import com.hvc.serverwscr02.socket.model.RoomUser;
import com.hvc.serverwscr02.socket.server.WebSocketServer;
import com.hvc.serverwscr02.socket.server.manager.SocketManager;
import com.hvc.serverwscr02.socket.services.service.SendServiceService;
import org.springframework.stereotype.Service;

@Service
public class SendServiceServiceImpl implements SendServiceService {

    @Override
    public void sendMessageToUser(String message, Users user) {
        if (checkForExistenceSerVer(user.getId())) {
            SocketManager.serverMap.get(user.getId()).sendMessage(message);
        }
    }

    @Override
    public void sendMessageToUserInRoom(String message, Room room) {
        room.getUserIdSet().forEach(idUser -> {
            if (checkForExistenceSerVer(idUser)) {
                getSerVer(idUser).sendMessage(message);
            }
        });
    }

    @Override
    public boolean checkForExistenceSerVer(Long key) {
        return SocketManager.serverMap.containsKey(key);
    }

    @Override
    public boolean checkForExistenceRoom(String key) {
        return SocketManager.roomMap.containsKey(key);
    }

    @Override
    public boolean checkUserExistenceRoom(String roomId, Long idUser) {
        RoomUser roomUser = SocketManager.roomUserMap.get(idUser);
        return roomUser != null && roomUser.getRoomIdSet().contains(roomId);
    }

    @Override
    public WebSocketServer getSerVer(Long key) {
        return SocketManager.serverMap.get(key);
    }

    @Override
    public Room getRoom(String key) {
        return SocketManager.roomMap.get(key);
    }


}
