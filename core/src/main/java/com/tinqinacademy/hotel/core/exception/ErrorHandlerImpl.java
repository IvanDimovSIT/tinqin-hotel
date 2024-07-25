package com.tinqinacademy.hotel.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;

@Component
public class ErrorHandlerImpl implements ErrorHandler {
    public ErrorWrapper handle(final MethodArgumentNotValidException exception) {
        ErrorWrapper errorWrapper = ErrorWrapper.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .errors(new ArrayList<>())
                .build();

        for(var error: exception.getBindingResult().getFieldErrors()){
            ErrorInfo errorInfo = ErrorInfo.builder()
                    .field(error.getField())
                    .message(error.getDefaultMessage())
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
            errorWrapper.add(errorInfo);
        }

        return errorWrapper;
    }
}
