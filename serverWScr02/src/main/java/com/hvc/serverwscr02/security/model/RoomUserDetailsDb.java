package com.hvc.serverwscr02.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hvc.serverwscr02.security.model.RoomDb;
import com.hvc.serverwscr02.security.model.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "room_user_detail")
public class RoomUserDetailsDb {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "roomId", updatable = false, insertable = false)
    @JsonIgnore
    RoomDb room;
    @Column
    String roomId;

    @ManyToOne
    @JoinColumn(name = "userId", updatable = false, insertable = false)
    @JsonIgnore
    Users user;
    @Column
    Long userId;


}
