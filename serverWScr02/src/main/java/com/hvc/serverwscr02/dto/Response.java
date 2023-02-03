package com.hvc.serverwscr02.dto;

import com.hvc.serverwscr02.utils.conts.Message;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Response<T> {
    boolean status;
    String message;
    T data;

    public Response(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public Response(String message) {
        this.message = message;
    }

    public Response(T data) {
        this.status = true;
        this.message = Message.SUCCESS;
        this.data = data;
    }
}
