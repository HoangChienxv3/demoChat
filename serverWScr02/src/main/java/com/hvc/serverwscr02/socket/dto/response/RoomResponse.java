package com.hvc.serverwscr02.socket.dto.response;

import com.hvc.serverwscr02.socket.model.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponse {
    String id;
    String name;

    public RoomResponse(Room room){
        this.id = room.getId();
        this.name = room.getNameRoom();
    }
}
