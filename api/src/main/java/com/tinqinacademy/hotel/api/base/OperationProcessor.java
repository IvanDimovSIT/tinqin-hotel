package com.tinqinacademy.hotel.api.base;

import com.tinqinacademy.hotel.api.errors.Errors;
import io.vavr.control.Either;

public interface OperationProcessor<I extends OperationInput, O extends OperationOutput> {
     Either<Errors, O> process(I input);
}
