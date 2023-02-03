package com.hvc.serverwscr02.socket.dto.response;

import com.hvc.serverwscr02.security.model.Users;
import com.hvc.serverwscr02.socket.model.Room;
import com.hvc.serverwscr02.utils.enums.MessageType;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ResponseMessage<T> {

    MessageType type;
    T content;
    SenderResponse sender;
    RoomResponse room;

    public ResponseMessage(T data, MessageType type, Users user, Room room) {
        this.type = type;
        this.content = data;
        this.sender = new SenderResponse(user);
        this.room = new RoomResponse(room);
    }
    public ResponseMessage(T data, MessageType type, Users user ) {
        this.type = type;
        this.content = data;
        this.sender = new SenderResponse(user);
    }
}
