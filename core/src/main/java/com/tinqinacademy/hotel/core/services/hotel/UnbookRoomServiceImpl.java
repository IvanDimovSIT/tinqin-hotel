package com.tinqinacademy.hotel.core.services.hotel;

import com.tinqinacademy.hotel.api.operations.hotel.unbookroom.UnbookRoomInput;
import com.tinqinacademy.hotel.api.operations.hotel.unbookroom.UnbookRoomOutput;
import com.tinqinacademy.hotel.api.operations.hotel.unbookroom.UnbookRoomService;
import com.tinqinacademy.hotel.core.exception.exceptions.NotFoundException;
import com.tinqinacademy.hotel.persistence.model.Booking;
import com.tinqinacademy.hotel.persistence.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UnbookRoomServiceImpl implements UnbookRoomService {
    private final BookingRepository bookingRepository;

    private Booking getBooking(String id) {
        return bookingRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new NotFoundException("Booking with id:" + id + " not found"));

    }

    @Override
    public UnbookRoomOutput process(UnbookRoomInput input) {
        log.info("Start unbookRoom input:{}", input);

        Booking booking = getBooking(input.getBookingId());

        bookingRepository.delete(booking);

        UnbookRoomOutput result = UnbookRoomOutput.builder()
                .build();

        log.info("End unbookRoom result:{}", result);

        return result;
    }
}
