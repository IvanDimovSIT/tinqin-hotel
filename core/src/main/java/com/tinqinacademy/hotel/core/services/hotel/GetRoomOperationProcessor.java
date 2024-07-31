package com.tinqinacademy.hotel.core.services.hotel;

import com.tinqinacademy.hotel.api.errors.Errors;
import com.tinqinacademy.hotel.api.operations.hotel.getroom.GetRoomInput;
import com.tinqinacademy.hotel.api.operations.hotel.getroom.GetRoomOutput;
import com.tinqinacademy.hotel.api.operations.hotel.getroom.GetRoomOperation;
import com.tinqinacademy.hotel.core.errors.ErrorMapper;
import com.tinqinacademy.hotel.core.exception.exceptions.NotFoundException;
import com.tinqinacademy.hotel.persistence.model.Booking;
import com.tinqinacademy.hotel.persistence.model.Room;
import com.tinqinacademy.hotel.persistence.repository.BookingRepository;
import com.tinqinacademy.hotel.persistence.repository.RoomRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetRoomOperationProcessor implements GetRoomOperation {
    private final ConversionService conversionService;
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final ErrorMapper errorMapper;

    private Room getRoom(String id) {
        return roomRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new NotFoundException("Room with id:" + id));
    }

    private List<LocalDate> findDatesOccupied(List<Booking> bookings) {
        List<LocalDate> datesOccupied = new ArrayList<>();

        for (Booking booking : bookings) {
            for (LocalDate date = booking.getStartDate(); !date.isAfter(booking.getEndDate()); date = date.plusDays(1)) {
                datesOccupied.add(date);
            }
        }
        Collections.sort(datesOccupied);

        return datesOccupied;
    }

    @Override
    public Either<Errors, GetRoomOutput> process(GetRoomInput input) {
        log.info("Start getRoom input:{}", input);

        Either<Errors, GetRoomOutput> result = Try.of(() -> {
                    Room room = getRoom(input.getId());

                    GetRoomOutput output = conversionService.convert(room, GetRoomOutput.class);

                    List<Booking> bookings = bookingRepository.findAllByRoomId(room.getId());
                    List<LocalDate> datesOccupied = findDatesOccupied(bookings);

                    output.setDatesOccupied(datesOccupied);
                    return output;
                })
                .toEither()
                .mapLeft(errorMapper::map);

        log.info("End getRoom result:{}", result);

        return result;
    }
}
