package com.tinqinacademy.hotel.core.response;

import com.tinqinacademy.hotel.api.base.OperationOutput;
import com.tinqinacademy.hotel.api.errors.Errors;
import io.vavr.control.Either;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface ResponseEntityMapper {
    <T extends OperationOutput> ResponseEntity<?> mapToResponseEntity(Either<Errors, T> either, HttpStatus status);
}
