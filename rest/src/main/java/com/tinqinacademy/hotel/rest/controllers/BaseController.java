package com.tinqinacademy.hotel.rest.controllers;

import com.tinqinacademy.hotel.api.base.OperationOutput;
import com.tinqinacademy.hotel.api.errors.Errors;
import io.vavr.control.Either;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public abstract class BaseController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    protected <T extends OperationOutput> ResponseEntity<?> mapToResponseEntity(Either<Errors, T> either, HttpStatus status) {
        log.info("Start mapToResponseEntity input: {}, status: {}", either, status);
        ResponseEntity<?> result;
        if (either.isRight()) {
            result = new ResponseEntity<>(either.get(), status);
        }else{
            Errors errors = either.getLeft();
            log.error(errors.toString());
            result = new ResponseEntity<>(errors.getErrorInfos(), errors.getStatus());
        }

        log.info("End mapToResponseEntity result: {}", result);

        return result;
    }
}
