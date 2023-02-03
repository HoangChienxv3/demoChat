package com.hvc.serverwscr02.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "room")
public class RoomDb {
    @Id
    String id;

    String nameRoom;
    Long ownerId;
}
