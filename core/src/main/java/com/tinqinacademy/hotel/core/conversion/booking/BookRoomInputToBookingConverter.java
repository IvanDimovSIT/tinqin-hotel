package com.tinqinacademy.hotel.core.conversion.booking;

import com.tinqinacademy.hotel.api.operations.hotel.bookroom.BookRoomInput;
import com.tinqinacademy.hotel.core.conversion.BaseConverter;
import com.tinqinacademy.hotel.persistence.model.Booking;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class BookRoomInputToBookingConverter extends BaseConverter<BookRoomInput, Booking> {
    @Override
    protected Booking convertObject(BookRoomInput source) {
        Booking booking = Booking.builder()
                .startDate(source.getStartDate())
                .endDate(source.getEndDate())
                .guests(new HashSet<>())
                .build();

        return booking;
    }
}
