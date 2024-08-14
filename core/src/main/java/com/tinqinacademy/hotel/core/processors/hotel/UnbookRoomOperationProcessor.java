package com.tinqinacademy.hotel.core.processors.hotel;

import com.tinqinacademy.hotel.api.errors.Errors;
import com.tinqinacademy.hotel.api.exception.exceptions.UnbookRoomException;
import com.tinqinacademy.hotel.api.operations.hotel.unbookroom.UnbookRoomInput;
import com.tinqinacademy.hotel.api.operations.hotel.unbookroom.UnbookRoomOutput;
import com.tinqinacademy.hotel.api.operations.hotel.unbookroom.UnbookRoomOperation;
import com.tinqinacademy.hotel.core.errors.ErrorMapper;
import com.tinqinacademy.hotel.api.exception.exceptions.NotFoundException;
import com.tinqinacademy.hotel.core.processors.BaseOperationProcessor;
import com.tinqinacademy.hotel.persistence.model.Booking;
import com.tinqinacademy.hotel.persistence.repository.BookingRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class UnbookRoomOperationProcessor extends BaseOperationProcessor implements UnbookRoomOperation {
    private final BookingRepository bookingRepository;

    public UnbookRoomOperationProcessor(ConversionService conversionService, ErrorMapper errorMapper,
                                        Validator validator, BookingRepository bookingRepository) {
        super(conversionService, errorMapper, validator);
        this.bookingRepository = bookingRepository;
    }


    private Booking getBooking(String id) {
        return bookingRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new NotFoundException("Booking with id:" + id));

    }

    private void checkUserCreatedBooking(Booking booking, String userId) {
        if (!booking.getUserId().toString().equals(userId)) {
            throw new UnbookRoomException();
        }
    }

    @Override
    public Either<Errors, UnbookRoomOutput> process(UnbookRoomInput input) {
        return Try.of(() -> {
                    log.info("Start unbookRoom input:{}", input);
                    validate(input);
                    Booking booking = getBooking(input.getBookingId());
                    checkUserCreatedBooking(booking, input.getUserId());

                    bookingRepository.delete(booking);

                    UnbookRoomOutput result = UnbookRoomOutput.builder()
                            .build();
                    log.info("End unbookRoom result:{}", result);

                    return result;
                })
                .toEither()
                .mapLeft(errorMapper::map);
    }
}
