package com.tinqinacademy.hotel.core.exception.exceptions;

import com.tinqinacademy.hotel.core.exception.BaseException;
import org.springframework.http.HttpStatus;

public class RegisterVisitorException extends BaseException {
    public RegisterVisitorException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
