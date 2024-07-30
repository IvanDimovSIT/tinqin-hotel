package com.tinqinacademy.hotel.core.exception.exceptions;


public class NotFoundException extends RuntimeException {
    public NotFoundException(String what) {
        super(what+" not found");
    }
}
