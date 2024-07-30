package com.tinqinacademy.hotel.core.services.hotel;

import com.tinqinacademy.hotel.api.errors.Errors;
import com.tinqinacademy.hotel.api.operations.hotel.getroom.GetRoomInput;
import com.tinqinacademy.hotel.api.operations.hotel.getroom.GetRoomOutput;
import com.tinqinacademy.hotel.api.operations.hotel.getroom.GetRoomOperation;
import com.tinqinacademy.hotel.core.exception.exceptions.NotFoundException;
import com.tinqinacademy.hotel.persistence.model.Booking;
import com.tinqinacademy.hotel.persistence.model.Room;
import com.tinqinacademy.hotel.persistence.repository.BookingRepository;
import com.tinqinacademy.hotel.persistence.repository.RoomRepository;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetRoomOperationProcessor implements GetRoomOperation {
    private final ConversionService conversionService;
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;

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

        Room room = getRoom(input.getId());

        GetRoomOutput result = conversionService.convert(room, GetRoomOutput.class);

        List<Booking> bookings = bookingRepository.findAllByRoomId(room.getId());
        List<LocalDate> datesOccupied = findDatesOccupied(bookings);

        result.setDatesOccupied(datesOccupied);

        log.info("End getRoom result:{}", result);

        return result;
    }
}
