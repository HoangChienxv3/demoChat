package com.hvc.serverwscr02.socket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room {
    String id;
    String nameRoom;
    Set<Long> userIdSet;
}
