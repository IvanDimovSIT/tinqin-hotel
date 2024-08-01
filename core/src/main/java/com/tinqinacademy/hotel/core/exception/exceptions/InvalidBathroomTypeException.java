package com.tinqinacademy.hotel.core.exception.exceptions;

import com.tinqinacademy.hotel.core.exception.BaseException;
import org.springframework.http.HttpStatus;

public class InvalidBathroomTypeException extends BaseException {
    public InvalidBathroomTypeException() {
        super("Entered BathroomType is invalid", HttpStatus.BAD_REQUEST);
    }
}
