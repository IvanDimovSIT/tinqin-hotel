package com.tinqinacademy.hotel.core.errors;

import com.tinqinacademy.hotel.api.errors.Errors;
import com.tinqinacademy.hotel.api.exception.BaseException;
import com.tinqinacademy.hotel.api.exception.exceptions.ViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;

@Component
public class ErrorMapperImpl implements ErrorMapper {


    private Errors convertExceptionHttpStatus(Throwable throwable) {
        BaseException exception = (BaseException) throwable;

        return Errors.builder()
                .error(throwable.getMessage(), exception.getHttpStatus())
                .build();
    }

    private Errors convertDefaultException(Throwable throwable) {
        return Errors.builder()
                .error(throwable.getMessage(), HttpStatus.BAD_REQUEST)
                .build();
    }

    private Errors convertViolationException(Throwable throwable) {
        ViolationException exception = (ViolationException) throwable;

        Errors.ErrorsBuilder errors = Errors.builder();
        exception.getErrors()
                .forEach(error -> errors.error(error, HttpStatus.BAD_REQUEST));

        return errors.build();

    }

    @Override
    public Errors map(Throwable throwable) {
        return Match(throwable).of(
                Case($(instanceOf(ViolationException.class)),
                        this::convertViolationException
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
