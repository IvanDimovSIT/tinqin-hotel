package com.tinqinacademy.hotel.core.exception.exceptions;

import com.tinqinacademy.hotel.core.exception.BaseException;
import org.springframework.http.HttpStatus;

public class InvalidBedSizeException extends BaseException {
    public InvalidBedSizeException(String bedSize) {
        super(String.format("Entered BedSize '%s' is invalid", bedSize), HttpStatus.BAD_REQUEST);
    }
}
