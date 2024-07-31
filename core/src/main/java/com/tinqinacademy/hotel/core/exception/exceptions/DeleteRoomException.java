package com.tinqinacademy.hotel.core.exception.exceptions;

import com.tinqinacademy.hotel.core.exception.BaseException;
import org.springframework.http.HttpStatus;

public class DeleteRoomException extends BaseException {
    public DeleteRoomException(String roomId) {
        super("Failed to delete room with id:" + roomId, HttpStatus.BAD_REQUEST);
    }
}
