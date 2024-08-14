package com.tinqinacademy.hotel.api.exception.exceptions;

import com.tinqinacademy.hotel.api.exception.BaseException;
import org.springframework.http.HttpStatus;

public class UnbookRoomException extends BaseException {

    public UnbookRoomException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
