package com.tinqinacademy.hotel.api.exception.exceptions;


import com.tinqinacademy.hotel.api.exception.BaseException;
import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseException {
    public NotFoundException(String what) {
        super(what+" not found", HttpStatus.NOT_FOUND);
    }
}
