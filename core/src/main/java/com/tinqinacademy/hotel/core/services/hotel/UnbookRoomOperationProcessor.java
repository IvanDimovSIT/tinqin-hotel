package com.tinqinacademy.hotel.core.services.hotel;

import com.tinqinacademy.hotel.api.errors.Errors;
import com.tinqinacademy.hotel.api.operations.hotel.unbookroom.UnbookRoomInput;
import com.tinqinacademy.hotel.api.operations.hotel.unbookroom.UnbookRoomOutput;
import com.tinqinacademy.hotel.api.operations.hotel.unbookroom.UnbookRoomOperation;
import com.tinqinacademy.hotel.core.errors.ErrorMapper;
import com.tinqinacademy.hotel.core.exception.exceptions.NotFoundException;
import com.tinqinacademy.hotel.persistence.model.Booking;
import com.tinqinacademy.hotel.persistence.repository.BookingRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;

@Service
@Slf4j
@RequiredArgsConstructor
public class UnbookRoomOperationProcessor implements UnbookRoomOperation {
    private final BookingRepository bookingRepository;
    private final ErrorMapper errorMapper;

    private Booking getBooking(String id) {
        return bookingRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new NotFoundException("Booking with id:" + id));

    }

    @Override
    public Either<Errors, UnbookRoomOutput> process(UnbookRoomInput input) {
        log.info("Start unbookRoom input:{}", input);

        Either<Errors, UnbookRoomOutput> result = Try.of(() -> {
                    Booking booking = getBooking(input.getBookingId());

                    bookingRepository.delete(booking);

                    return UnbookRoomOutput.builder()
                            .build();
                })
                .toEither()
                .mapLeft(errorMapper::map);

        log.info("End unbookRoom result:{}", result);

        return result;
    }
}
