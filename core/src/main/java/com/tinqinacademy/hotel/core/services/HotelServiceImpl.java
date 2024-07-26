package com.tinqinacademy.hotel.core.services;


import com.tinqinacademy.hotel.api.operations.hotel.bookroom.BookRoomInput;
import com.tinqinacademy.hotel.api.operations.hotel.bookroom.BookRoomOutput;
import com.tinqinacademy.hotel.api.operations.hotel.checkavailablerooms.CheckAvailableRoomsInput;
import com.tinqinacademy.hotel.api.operations.hotel.checkavailablerooms.CheckAvailableRoomsOutput;
import com.tinqinacademy.hotel.api.operations.hotel.getroom.GetRoomInput;
import com.tinqinacademy.hotel.api.operations.hotel.getroom.GetRoomOutput;
import com.tinqinacademy.hotel.api.operations.hotel.unbookroom.UnbookRoomInput;
import com.tinqinacademy.hotel.api.operations.hotel.unbookroom.UnbookRoomOutput;
import com.tinqinacademy.hotel.core.exception.exceptions.BookRoomException;
import com.tinqinacademy.hotel.core.exception.exceptions.NotFoundException;
import com.tinqinacademy.hotel.api.services.HotelService;
import com.tinqinacademy.hotel.persistence.model.Booking;
import com.tinqinacademy.hotel.persistence.model.Room;
import com.tinqinacademy.hotel.persistence.model.User;
import com.tinqinacademy.hotel.persistence.repository.BookingRepository;
import com.tinqinacademy.hotel.persistence.repository.RoomRepository;
import com.tinqinacademy.hotel.persistence.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;


@Service
@AllArgsConstructor
@Slf4j
public class HotelServiceImpl implements HotelService {
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ConversionService conversionService;

    @Override
    public GetRoomOutput getRoom(final GetRoomInput input) {
        log.info("Start getRoom input:{}", input);

        Room room = roomRepository.findById(UUID.fromString(input.getId()))
                .orElseThrow(() -> new NotFoundException("Room not found with id:" + input.getId()));

        GetRoomOutput result = conversionService.convert(room, GetRoomOutput.class);

        List<LocalDate> datesOccupied = new ArrayList<>();
        List<Booking> bookings = bookingRepository.findAllByRoomId(room.getId());

        for (Booking booking : bookings) {
            for (LocalDate date = booking.getStartDate(); !date.isAfter(booking.getEndDate()); date = date.plusDays(1)) {
                datesOccupied.add(date);
            }
        }
        Collections.sort(datesOccupied);
        result.setDatesOccupied(datesOccupied);

        log.info("End getRoom result:{}", result);

        return result;
    }


    @Override
    public CheckAvailableRoomsOutput checkAvailableRooms(final CheckAvailableRoomsInput input) {
        log.info("Start checkAvailableRooms input:{}", input);

        var bathroomTypeCriteria = input.getBathroomType() == null ? null :
                com.tinqinacademy.hotel.persistence.model.enums.BathroomType
                        .getCode(input.getBathroomType().toString());

        var bedSizeCriteria = input.getBedSize() == null ? null :
                com.tinqinacademy.hotel.persistence.model.enums.BedSize
                        .getCode(input.getBedSize().toString());

        List<Room> rooms = roomRepository.findAvailableRooms(
                input.getStartDate(),
                input.getEndDate(),
                bathroomTypeCriteria,
                bedSizeCriteria,
                input.getBedCount());

        CheckAvailableRoomsOutput result = conversionService.convert(rooms, CheckAvailableRoomsOutput.class);
        log.info("End checkAvailableRooms result:{}", result);

        return result;
    }

    @Override
    public BookRoomOutput bookRoom(final BookRoomInput input) {
        log.info("Start bookRoom input:{},", input);
        Room room = roomRepository.findById(UUID.fromString(input.getId())).orElseThrow(
                () -> new NotFoundException("Room not found with id:" + input.getId()));

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

    @Override
    public UnbookRoomOutput unbookRoom(UnbookRoomInput input) {
        log.info("Start unbookRoom input:{}", input);

        Booking booking = bookingRepository.findById(UUID.fromString(input.getBookingId())).orElseThrow(
                () -> new NotFoundException("Booking with id:" + input.getBookingId() + " not found"));

        bookingRepository.delete(booking);

        UnbookRoomOutput result = UnbookRoomOutput.builder()
                .build();

        log.info("End unbookRoom result:{}", result);

        return result;
    }

}
