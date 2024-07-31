package com.tinqinacademy.hotel.core.exception.exceptions;


import com.tinqinacademy.hotel.core.exception.BaseException;
import org.springframework.http.HttpStatus;

public class CreateRoomException extends BaseException {
    public CreateRoomException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
