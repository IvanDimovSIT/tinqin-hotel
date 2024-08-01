package com.tinqinacademy.hotel.core.exception.exceptions;


import com.tinqinacademy.hotel.core.exception.BaseException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class ViolationException extends RuntimeException {
    private final List<String> errors;

    public ViolationException(List<String> errors) {
        this.errors = errors;
    }
}
