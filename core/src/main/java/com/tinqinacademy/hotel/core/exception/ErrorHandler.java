package com.tinqinacademy.hotel.core.exception;

import org.springframework.web.bind.MethodArgumentNotValidException;

public interface ErrorHandler {
    ErrorWrapper handle(final MethodArgumentNotValidException exception);
}
