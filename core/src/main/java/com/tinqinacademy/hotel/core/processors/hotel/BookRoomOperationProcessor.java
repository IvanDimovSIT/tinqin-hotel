package com.tinqinacademy.hotel.core.processors.hotel;

import com.tinqinacademy.hotel.api.errors.Errors;
import com.tinqinacademy.hotel.api.operations.hotel.bookroom.BookRoomInput;
import com.tinqinacademy.hotel.api.operations.hotel.bookroom.BookRoomOutput;
import com.tinqinacademy.hotel.api.operations.hotel.bookroom.BookRoomOperation;
import com.tinqinacademy.hotel.core.errors.ErrorMapper;
import com.tinqinacademy.hotel.core.exception.exceptions.BookRoomException;
import com.tinqinacademy.hotel.core.exception.exceptions.NotFoundException;
import com.tinqinacademy.hotel.core.processors.BaseOperationProcessor;
import com.tinqinacademy.hotel.persistence.model.Booking;
import com.tinqinacademy.hotel.persistence.model.Room;
import com.tinqinacademy.hotel.persistence.model.User;
import com.tinqinacademy.hotel.persistence.repository.BookingRepository;
import com.tinqinacademy.hotel.persistence.repository.RoomRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
@Slf4j
public class BookRoomOperationProcessor extends BaseOperationProcessor implements BookRoomOperation {
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;

    public BookRoomOperationProcessor(ConversionService conversionService, ErrorMapper errorMapper, Validator validator,
                                      RoomRepository roomRepository, BookingRepository bookingRepository) {
        super(conversionService, errorMapper, validator);
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
    }


    private Room getRoom(String id) {
        return roomRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new NotFoundException("Room with id:" + id));
    }

    private void checkRoomOccupied(Room room, LocalDate startDate, LocalDate endDate) {
        if (bookingRepository.checkRoomOccupied(room.getId(), startDate, endDate)) {
            throw new BookRoomException("Room with id:" + room.getId() + " occupied in the date range (" +
                    endDate + " : " + startDate + ")");
        }
    }

    private User convertInputToUser(BookRoomInput bookRoomInput) {
        return conversionService.convert(bookRoomInput, User.class);
    }

    private Booking convertInputToBooking(BookRoomInput bookRoomInput) {
        return conversionService.convert(bookRoomInput, Booking.class);
    }

    private Booking saveBookingWithRoomAndUser(Booking booking, Room room, User user, LocalDate startDate, LocalDate endDate) {
        booking.setUser(user);
        booking.setRoom(room);
        booking.setTotalPrice(room.getPrice().multiply(BigDecimal.valueOf(endDate.toEpochDay() -
                startDate.toEpochDay())));

        return bookingRepository.save(booking);
    }

    @Override
    public Either<Errors, BookRoomOutput> process(BookRoomInput input) {
        log.info("Start bookRoom input:{},", input);
        Either<Errors, BookRoomOutput> result = Try.of(() -> {
                    validate(input);
                    Room room = getRoom(input.getId());
                    checkRoomOccupied(room, input.getStartDate(), input.getEndDate());
                    User user = convertInputToUser(input);
                    Booking booking = convertInputToBooking(input);
                    Booking savedBooking = saveBookingWithRoomAndUser(
                            booking, room, user, input.getStartDate(), input.getEndDate());

                    BookRoomOutput bookRoomOutput = BookRoomOutput.builder()
                            .build();

                    return bookRoomOutput;
                })
                .toEither()
                .mapLeft(errorMapper::map);

        log.info("End bookRoom result:{}", result);
        return result;
    }
}