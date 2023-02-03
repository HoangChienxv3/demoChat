package com.hvc.serverwscr02.socket.services.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hvc.serverwscr02.security.model.Users;
import com.hvc.serverwscr02.socket.dto.request.RequestMessage;

public interface RoomMessageService {

    void createRoom(RequestMessage requestMessage, Users user) throws JsonProcessingException;

    void joinRoom(RequestMessage requestMessage, Users user) throws JsonProcessingException;

    void getListRoomOfUser(RequestMessage requestMessage, Users user) throws JsonProcessingException;

    void leaveRoom(RequestMessage requestMessage, Users user) throws JsonProcessingException;

    void updateRoom(RequestMessage requestMessage, Users user) throws JsonProcessingException;

    void deleteRoom(RequestMessage requestMessage, Users user);


}
