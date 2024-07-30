package com.tinqinacademy.hotel.core.services.hotel;

import com.tinqinacademy.hotel.api.operations.hotel.bookroom.BookRoomInput;
import com.tinqinacademy.hotel.api.operations.hotel.bookroom.BookRoomOutput;
import com.tinqinacademy.hotel.api.operations.hotel.bookroom.BookRoomService;
import com.tinqinacademy.hotel.core.exception.exceptions.BookRoomException;
import com.tinqinacademy.hotel.core.exception.exceptions.NotFoundException;
import com.tinqinacademy.hotel.persistence.model.Booking;
import com.tinqinacademy.hotel.persistence.model.Room;
import com.tinqinacademy.hotel.persistence.model.User;
import com.tinqinacademy.hotel.persistence.repository.BookingRepository;
import com.tinqinacademy.hotel.persistence.repository.RoomRepository;
import com.tinqinacademy.hotel.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookRoomServiceImpl implements BookRoomService {
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ConversionService conversionService;

    private Room getRoom(String id) {
        return roomRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new NotFoundException("Room with id:" + id));
    }

    @Override
    public BookRoomOutput process(BookRoomInput input) {
        log.info("Start bookRoom input:{},", input);
        Room room = getRoom(input.getId());

        if(bookingRepository.checkRoomOccupied(room.getId(), input.getStartDate(), input.getEndDate())){
            throw new BookRoomException("Room with id:"+room.getId()+" occupied in the date range ("+
                    input.getStartDate()+" : "+input.getEndDate()+")");
        }

        User user = conversionService.convert(input, User.class);
        user = userRepository.save(user);

        Booking booking = conversionService.convert(input, Booking.class);
        booking.setUser(user);
        booking.setRoom(room);
        booking.setTotalPrice(room.getPrice().multiply(BigDecimal.valueOf(input.getEndDate().toEpochDay() -
                input.getStartDate().toEpochDay())));

        bookingRepository.save(booking);

        BookRoomOutput bookRoomOutput = BookRoomOutput.builder()
                .build();

        log.info("End bookRoom result:{}", bookRoomOutput);

        return bookRoomOutput;
    }
}
