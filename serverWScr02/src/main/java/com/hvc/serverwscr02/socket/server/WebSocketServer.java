package com.hvc.serverwscr02.socket.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hvc.serverwscr02.security.model.Users;
import com.hvc.serverwscr02.socket.dto.request.RequestMessage;
import com.hvc.serverwscr02.socket.server.manager.SocketManager;
import com.hvc.serverwscr02.socket.services.service.ChatMessageService;
import com.hvc.serverwscr02.socket.services.service.RoomMessageService;
import com.hvc.serverwscr02.socket.valid.ValidTokenWs;
import com.hvc.serverwscr02.utils.BeanUtils;
import com.hvc.serverwscr02.utils.enums.MessageType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@Data
@ServerEndpoint(value = "/websocket/{token}")
public class WebSocketServer {

    ObjectMapper objectMapper = new ObjectMapper();

    private Session session;

    private Users user;

    RoomMessageService roomMessageService;
    ChatMessageService chatMessageService;


    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {

        ValidTokenWs validTokenWs = BeanUtils.getBean(ValidTokenWs.class);
        roomMessageService = BeanUtils.getBean(RoomMessageService.class);
        chatMessageService = BeanUtils.getBean(ChatMessageService.class);

        this.session = session;
        this.user = validTokenWs.validTokenWs(token);
        if (SocketManager.serverMap.containsKey(user.getId())) {
            SocketManager.serverMap.remove(user.getId());
            SocketManager.serverMap.put(user.getId(), this);
        } else {
            SocketManager.serverMap.put(user.getId(), this);
            addOnlineCount();
            log.info(user.getUsername() + "，Online!");
        }
        sendMessage(String.valueOf(getOnlineCount()));
    }

    @OnMessage
    public void onMessage(String message, Session session) throws JsonProcessingException {

        RequestMessage requestMessage = objectMapper.readValue(message, RequestMessage.class);
        log.info("The server received a user request " + user.getUsername() + " Message from:" + message);

        switch (requestMessage.getType()) {
            case CHAT:
                chatMessageService.chatRoom(requestMessage, user);
                break;
            case CREATE_ROOM:
                roomMessageService.createRoom(requestMessage, user);
                break;
            case JOIN_ROOM:
                roomMessageService.joinRoom(requestMessage, user);
                break;
            case GET_LIST_USER_ROOM:
                roomMessageService.getListUserRoom(requestMessage, user);
                break;
            case UPDATE_ROOM:
                roomMessageService.updateRoom(requestMessage, user);
                break;
            case LEAVE_ROOM:
                System.out.println(message + MessageType.LEAVE_ROOM);
                break;
            case DELETE_USER_ROOM:
                System.out.println(message + MessageType.DELETE_USER_ROOM);
                break;
            case DELETE_ROOM:
                System.out.println(message + MessageType.DELETE_ROOM);
                break;
        }
    }

    /**
     * The server actively sends messages
     *
     * @param message news
     */
    public void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


    /**
     * Get online people
     *
     * @return Number of people online
     */
    public static int getOnlineCount() {
        return SocketManager.onlineCount;
    }

    @OnClose
    public void onClose() {
        if (SocketManager.serverMap.containsKey(user.getId())) {
            SocketManager.serverMap.remove(user.getId());
            subOnlineCount();
            log.info(user.getUsername() + "，Offline!");
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("user " + user.getUsername() + " An error occurred as follows: " + throwable.getMessage());
    }

    private static synchronized void subOnlineCount() {
        SocketManager.onlineCount--;
    }

    public static synchronized void addOnlineCount() {
        SocketManager.onlineCount++;
    }

    public static WebSocketServer get(String userNo) {
        return SocketManager.serverMap.get(userNo);
    }

    public static Map<Long, WebSocketServer> getMap() {
        return SocketManager.serverMap;
    }

    public static boolean isOnline(String userNo) {
        return SocketManager.serverMap.containsKey(userNo);
    }
}