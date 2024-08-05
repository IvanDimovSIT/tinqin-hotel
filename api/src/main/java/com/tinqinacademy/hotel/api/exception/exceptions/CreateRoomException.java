package com.tinqinacademy.hotel.api.exception.exceptions;


import com.tinqinacademy.hotel.api.exception.BaseException;
import org.springframework.http.HttpStatus;

public class CreateRoomException extends BaseException {
    public CreateRoomException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
