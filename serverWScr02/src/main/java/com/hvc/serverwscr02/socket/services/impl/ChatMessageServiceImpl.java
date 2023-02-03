package com.hvc.serverwscr02.socket.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hvc.serverwscr02.security.model.Users;
import com.hvc.serverwscr02.socket.dto.request.RequestMessage;
import com.hvc.serverwscr02.socket.dto.response.ResponseMessage;
import com.hvc.serverwscr02.socket.services.service.ChatMessageService;
import com.hvc.serverwscr02.socket.services.service.SendServiceService;
import com.hvc.serverwscr02.utils.conts.Message;
import com.hvc.serverwscr02.utils.enums.MessageType;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ChatMessageServiceImpl implements ChatMessageService {

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    SendServiceService sendServiceService;

    @SneakyThrows
    @Override
    public void chatRoom(RequestMessage requestMessage, Users user) {
        String json;
        if (requestMessage.getContent() == null) {
            return;
        }
        if (!sendServiceService.checkForExistenceRoom(requestMessage.getIdRoom())) {
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

        json = objectMapper.writeValueAsString(new ResponseMessage<>(
                requestMessage.getContent(),
                MessageType.ERROR,
                user,
                sendServiceService.getRoom(requestMessage.getIdRoom())
        ));
        sendServiceService.sendMessageToUserInRoom(json, sendServiceService.getRoom(requestMessage.getIdRoom()));
    }
}
