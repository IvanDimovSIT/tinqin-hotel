package com.tinqinacademy.hotel.api.exception.exceptions;

import com.tinqinacademy.hotel.api.exception.BaseException;
import org.springframework.http.HttpStatus;

public class BookRoomException extends BaseException {
    public BookRoomException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
