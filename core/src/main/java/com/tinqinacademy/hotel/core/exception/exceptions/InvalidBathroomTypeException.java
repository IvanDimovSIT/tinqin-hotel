package com.tinqinacademy.hotel.core.exception.exceptions;

import com.tinqinacademy.hotel.core.exception.BaseException;
import org.springframework.http.HttpStatus;

public class InvalidBathroomTypeException extends BaseException {
    public InvalidBathroomTypeException(String bathroomType) {
        super(String.format("Entered BathroomType '%s' is invalid", bathroomType), HttpStatus.BAD_REQUEST);
    }
}
