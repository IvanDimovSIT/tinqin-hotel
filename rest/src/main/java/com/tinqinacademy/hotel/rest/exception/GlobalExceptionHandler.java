package com.tinqinacademy.hotel.rest.exception;

import com.tinqinacademy.hotel.core.exception.ErrorHandler;
import com.tinqinacademy.hotel.core.exception.ErrorWrapper;
import com.tinqinacademy.hotel.core.exception.exceptions.BookRoomException;
import com.tinqinacademy.hotel.core.exception.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {
    private final ErrorHandler errorHandler;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException exception) {
        ErrorWrapper errorWrapper = errorHandler.handle(exception);

        log.error(errorWrapper.toString());
        return new ResponseEntity<>(errorWrapper.getErrors(), errorWrapper.getHttpStatus());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException exception) {
        log.error(exception.toString());
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookRoomException.class)
    public ResponseEntity<?> handleBookRoomException(BookRoomException exception) {
        log.error(exception.toString());
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }


}
