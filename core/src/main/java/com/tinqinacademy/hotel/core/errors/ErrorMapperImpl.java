package com.tinqinacademy.hotel.core.errors;

import com.tinqinacademy.hotel.api.errors.Errors;
import com.tinqinacademy.hotel.core.exception.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;

@Component
public class ErrorMapperImpl implements ErrorMapper {

    @Override
    public Errors map(Throwable throwable) {
        return Match(throwable).of(
                Case($(instanceOf(NotFoundException.class)), Errors.builder()
                        .error(throwable.getMessage(), HttpStatus.NOT_FOUND)
                        .build()
                ),
                Case($(instanceOf(DeleteRoomException.class)), Errors.builder()
                        .error(throwable.getMessage(), HttpStatus.BAD_REQUEST)
                        .build()
                ),
                Case($(instanceOf(CreateRoomException.class)), Errors.builder()
                        .error(throwable.getMessage(), HttpStatus.BAD_REQUEST)
                        .build()
                ),
                Case($(instanceOf(PartialUpdateRoomException.class)), Errors.builder()
                        .error(throwable.getMessage(), HttpStatus.BAD_REQUEST)
                        .build()
                ),
                Case($(instanceOf(RegisterVisitorException.class)), Errors.builder()
                        .error(throwable.getMessage(), HttpStatus.BAD_REQUEST)
                        .build()
                ),
                Case($(), Errors.builder()
                        .error(throwable.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR)
                        .build()
                )
        );
    }
}
