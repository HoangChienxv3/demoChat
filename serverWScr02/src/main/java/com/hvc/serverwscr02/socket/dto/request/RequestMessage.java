package com.hvc.serverwscr02.socket.dto.request;

import com.hvc.serverwscr02.utils.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestMessage {
    private MessageType type;
    private String content;
    private String idRoom;
    private Long idPartner;

}
