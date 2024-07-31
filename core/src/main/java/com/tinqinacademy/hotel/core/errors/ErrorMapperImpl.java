package com.tinqinacademy.hotel.core.errors;

import com.tinqinacademy.hotel.api.errors.Errors;
import com.tinqinacademy.hotel.core.exception.BaseException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;

@Component
public class ErrorMapperImpl implements ErrorMapper {

    private Errors convertConstraintViolationException(Throwable throwable) {
        ConstraintViolationException exception = (ConstraintViolationException) throwable;
        Errors.ErrorsBuilder errors = Errors.builder();
        exception.getConstraintViolations()
                .forEach(violation -> errors.error(violation.getMessage(), HttpStatus.BAD_REQUEST));

        return errors.build();
    }

    private Errors convertExceptionHttpStatus(Throwable throwable) {
        BaseException exception = (BaseException) throwable;

        return Errors.builder()
                .error(throwable.getMessage(), exception.getHttpStatus())
                .build();
    }

    private Errors convertDefaultException(Throwable throwable) {
        return Errors.builder()
                .error(throwable.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }

    @Override
    public Errors map(Throwable throwable) {
        return Match(throwable).of(
                Case($(instanceOf(ConstraintViolationException.class)),
                        this::convertConstraintViolationException
                ),
                Case($(instanceOf(BaseException.class)),
                        this::convertExceptionHttpStatus
                ),
                Case($(),
                        this::convertDefaultException
                )
        );
    }
}
