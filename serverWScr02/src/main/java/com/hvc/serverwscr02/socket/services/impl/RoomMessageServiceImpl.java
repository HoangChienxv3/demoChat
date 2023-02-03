package com.hvc.serverwscr02.socket.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hvc.serverwscr02.dto.UserResponse;
import com.hvc.serverwscr02.security.model.Users;
import com.hvc.serverwscr02.security.service.service.UserService;
import com.hvc.serverwscr02.socket.dto.request.RequestMessage;
import com.hvc.serverwscr02.socket.dto.response.ResponseMessage;
import com.hvc.serverwscr02.socket.dto.response.RoomResponse;
import com.hvc.serverwscr02.socket.model.Room;
import com.hvc.serverwscr02.socket.model.RoomUser;
import com.hvc.serverwscr02.socket.server.manager.SocketManager;
import com.hvc.serverwscr02.socket.services.service.RoomMessageService;
import com.hvc.serverwscr02.socket.services.service.SendServiceService;
import com.hvc.serverwscr02.utils.RandomStringExmple;
import com.hvc.serverwscr02.utils.conts.Message;
import com.hvc.serverwscr02.utils.enums.MessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RoomMessageServiceImpl implements RoomMessageService {

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    SendServiceService sendServiceService;

    @Autowired
    UserService userService;

    @Override
    public void createRoom(RequestMessage requestMessage, Users user) throws JsonProcessingException {
        Room room = createNewRoom(requestMessage, user);
        createOrUpdateInfo(room, user);
        String json = objectMapper.writeValueAsString(new ResponseMessage<>(
                Message.SUCCESSFUL_ROOM_CREATEION,
                MessageType.CREATE_ROOM,
                user,
                room
        ));
        sendServiceService.sendMessageToUserInRoom(json, room);
    }

    @Override
    public void joinRoom(RequestMessage requestMessage, Users user) throws JsonProcessingException {
        if (!sendServiceService.checkForExistenceRoom(requestMessage.getIdRoom())) {
            String json = objectMapper.writeValueAsString(new ResponseMessage<>(
                    Message.ROOM_DOES_NOT_EXIST,
                    MessageType.ERROR,
                    user
            ));
            sendServiceService.sendMessageToUser(json, user);
            return;
        }
        if (!sendServiceService.checkUserExistenceRoom(requestMessage.getIdRoom(), user.getId())) {
            String json = objectMapper.writeValueAsString(new ResponseMessage<>(
                    Message.YOU_HAVE_NO_RIGHTS_TO_THIS_ACTION,
                    MessageType.ERROR,
                    user
            ));
            sendServiceService.sendMessageToUser(json, user);
            return;
        }
        Users userPartner = userService.findUserByUserId(requestMessage.getIdPartner());
        if (userPartner == null) {
            String json = objectMapper.writeValueAsString(new ResponseMessage<>(
                    Message.PARTNER_DOSE_NOT_EXIST,
                    MessageType.ERROR,
                    user
            ));
            sendServiceService.sendMessageToUser(json, user);
            return;

        }
        createOrUpdateInfo(SocketManager.roomMap.get(requestMessage.getIdRoom()), userPartner);
        String json = objectMapper.writeValueAsString(new ResponseMessage<>(
                Message.JOIN,
                MessageType.JOIN_ROOM,
                user,
                sendServiceService.getRoom(requestMessage.getIdRoom())
        ));
        sendServiceService.sendMessageToUserInRoom(json, sendServiceService.getRoom(requestMessage.getIdRoom()));
    }

    @Override
    public void getListRoomOfUser(RequestMessage requestMessage, Users user) throws JsonProcessingException {
        RoomUser roomUser = SocketManager.roomUserMap.get(user.getId());
        String json;
        if (roomUser == null) {
            json = objectMapper.writeValueAsString(new ResponseMessage<>(
                    Message.YOU_HAVE_NOT_JOINED_AND_ROOMS_YET,
                    MessageType.ERROR,
                    user
            ));
            sendServiceService.sendMessageToUser(json, user);
            return;
        }
        List<RoomResponse> roomList = new ArrayList<>();
        roomUser.getRoomIdSet().forEach(idRoom -> {
            if (sendServiceService.checkForExistenceRoom(idRoom)) {
                roomList.add(new RoomResponse(sendServiceService.getRoom(idRoom)));
            }
        });
        json = objectMapper.writeValueAsString(new ResponseMessage<>(
                roomList,
                MessageType.GET_LIST_ROOM_OF_USER,
                user
        ));
        sendServiceService.sendMessageToUser(json, user);
    }

    @Override
    public void getListUserInRoom(RequestMessage requestMessage, Users user) throws JsonProcessingException {
        Room room = sendServiceService.getRoom(requestMessage.getIdRoom());
        String json;
        if (room == null) {
            json = objectMapper.writeValueAsString(new ResponseMessage<>(
                    Message.ROOM_DOES_NOT_EXIST,
                    MessageType.ERROR,
                    user
            ));
            sendServiceService.sendMessageToUser(json, user);
            return;
        }
        if (!sendServiceService.checkUserExistenceRoom(requestMessage.getIdRoom(), user.getId())) {
            json = objectMapper.writeValueAsString(new ResponseMessage<>(
                    Message.YOU_HAVE_NO_RIGHTS_TO_THIS_ACTION,
                    MessageType.ERROR,
                    user
            ));
            sendServiceService.sendMessageToUser(json, user);
            return;
        }
        List<Users> usersList = userService.findAllByIdIn(room.getUserIdSet());
        json = objectMapper.writeValueAsString(new ResponseMessage<>(
                usersList.stream().map(UserResponse::new).collect(Collectors.toList()),
                MessageType.GET_LIST_USER_IN_ROOM,
                user,
                room
        ));
        sendServiceService.sendMessageToUser(json, user);
    }

    @Override
    public void leaveRoom(RequestMessage requestMessage, Users user) throws JsonProcessingException {
        Room room = sendServiceService.getRoom(requestMessage.getIdRoom());
        if (room == null) {
            String json = objectMapper.writeValueAsString(new ResponseMessage<>(
                    Message.ROOM_DOES_NOT_EXIST,
                    MessageType.ERROR,
                    user
            ));
            sendServiceService.sendMessageToUser(json, user);
            return;
        }
        if (!sendServiceService.checkUserExistenceRoom(requestMessage.getIdRoom(), user.getId())) {
            String json = objectMapper.writeValueAsString(new ResponseMessage<>(
                    Message.YOU_HAVE_NO_RIGHTS_TO_THIS_ACTION,
                    MessageType.ERROR,
                    user
            ));
            sendServiceService.sendMessageToUser(json, user);
            return;
        }
        deleteUserRoom(room, user);
        String json = objectMapper.writeValueAsString(new ResponseMessage<>(
                user.getUsername() + " " + Message.LEFT_THE_ROOM,
                MessageType.UPDATE_ROOM,
                user,
                sendServiceService.getRoom(requestMessage.getIdRoom())
        ));
        sendServiceService.sendMessageToUserInRoom(json, sendServiceService.getRoom(requestMessage.getIdRoom()));
    }

    @Override
    public void updateRoom(RequestMessage requestMessage, Users user) throws JsonProcessingException {
        Room room = sendServiceService.getRoom(requestMessage.getIdRoom());
        if (room == null) {
            String json = objectMapper.writeValueAsString(new ResponseMessage<>(
                    Message.ROOM_DOES_NOT_EXIST,
                    MessageType.ERROR,
                    user
            ));
            sendServiceService.sendMessageToUser(json, user);
            return;
        }
        if (!sendServiceService.checkUserExistenceRoom(requestMessage.getIdRoom(), user.getId())
                || !Objects.equals(user.getId(), room.getOwner())) {
            String json = objectMapper.writeValueAsString(new ResponseMessage<>(
                    Message.YOU_HAVE_NO_RIGHTS_TO_THIS_ACTION,
                    MessageType.ERROR,
                    user
            ));
            sendServiceService.sendMessageToUser(json, user);
            return;
        }
        room.setNameRoom(requestMessage.getContent());
        createOrUpdateInfo(room, user);
        String json = objectMapper.writeValueAsString(new ResponseMessage<>(
                room,
                MessageType.UPDATE_ROOM,
                user,
                sendServiceService.getRoom(requestMessage.getIdRoom())
        ));
        sendServiceService.sendMessageToUserInRoom(json, sendServiceService.getRoom(requestMessage.getIdRoom()));
    }

    @Override
    public void deleteRoom(RequestMessage requestMessage, Users user) {

    }


    Room createNewRoom(RequestMessage requestMessage, Users user) {
        Room room = new Room();
        String idRoom = RandomStringExmple.randomAlphaNumeric(12);
        while (sendServiceService.checkForExistenceRoom(idRoom)) {
            idRoom = RandomStringExmple.randomAlphaNumeric(12);
        }
        room.setOwner(user.getId());
        room.setId(idRoom);
        room.setNameRoom(requestMessage.getContent() != null ? requestMessage.getContent() : null);
        return room;
    }

    void createOrUpdateInfo(Room room, Users user) {
        createOrUpdateRoomList(room, user);
        createOrUpdateUserRoomList(room, user);
    }

    void createOrUpdateRoomList(Room room, Users user) {
        Set<Long> userIdSet = new HashSet<>();
        if (sendServiceService.checkForExistenceRoom(room.getId())) {
            Room r = SocketManager.roomMap.get(room.getId());
            userIdSet = r.getUserIdSet();
        }
        userIdSet.add(user.getId());
        room.setUserIdSet(userIdSet);
        SocketManager.roomMap.put(room.getId(), room);

    }

    void createOrUpdateUserRoomList(Room room, Users user) {
        RoomUser roomUser = new RoomUser();
        Set<String> roomIdSet = new HashSet<>();
        if (SocketManager.roomUserMap.containsKey(user.getId())) {
            roomUser = SocketManager.roomUserMap.get(user.getId());
            roomIdSet = roomUser.getRoomIdSet();
        }
        roomIdSet.add(room.getId());
        roomUser.setRoomIdSet(roomIdSet);
        SocketManager.roomUserMap.put(user.getId(), roomUser);
    }

    void deleteUserRoom(Room room, Users user) {
        deleteUserInRoomList(room, user);
        deleteUserInUserRoomList(room, user);
    }

    void deleteUserInRoomList(Room room, Users user) {
        room.getUserIdSet().remove(user.getId());
        SocketManager.roomMap.put(room.getId(), room);
    }

    void deleteUserInUserRoomList(Room room, Users user) {
        RoomUser roomUser = SocketManager.roomUserMap.get(user.getId());
        roomUser.getRoomIdSet().remove(room.getId());
        SocketManager.roomUserMap.put(user.getId(), roomUser);
    }

    void CheckValidInput(RequestMessage message, Users user) {

    }
}
