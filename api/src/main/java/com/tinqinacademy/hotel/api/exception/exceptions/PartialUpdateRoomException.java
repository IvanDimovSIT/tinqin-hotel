package com.tinqinacademy.hotel.api.exception.exceptions;

import com.tinqinacademy.hotel.api.exception.BaseException;
import org.springframework.http.HttpStatus;

public class PartialUpdateRoomException extends BaseException {
    public PartialUpdateRoomException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
