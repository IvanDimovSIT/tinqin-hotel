package com.tinqinacademy.hotel.core.exception.exceptions;

import com.tinqinacademy.hotel.core.exception.BaseException;
import org.springframework.http.HttpStatus;

public class InvalidBedSizeException extends BaseException {
    public InvalidBedSizeException() {
        super("Entered BedSize '%s' is invalid", HttpStatus.BAD_REQUEST);
    }
}
