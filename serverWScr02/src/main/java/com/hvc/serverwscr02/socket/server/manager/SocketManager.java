package com.hvc.serverwscr02.socket.server.manager;

import com.hvc.serverwscr02.socket.model.Room;
import com.hvc.serverwscr02.socket.model.RoomUser;
import com.hvc.serverwscr02.socket.server.WebSocketServer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SocketManager {
    //connection of user
    public static Map<Long, WebSocketServer> serverMap = new ConcurrentHashMap<>();
    //Map room
    public static Map<String, Room> roomMap = new ConcurrentHashMap<>();
    //Map room of user
    public static Map<Long, RoomUser> roomUserMap = new ConcurrentHashMap<>();
    public static int onlineCount = 0;

}
