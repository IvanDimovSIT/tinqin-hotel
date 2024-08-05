package com.tinqinacademy.hotel.core.processors.system;

import com.tinqinacademy.hotel.api.errors.Errors;
import com.tinqinacademy.hotel.api.operations.system.deleteroom.DeleteRoomInput;
import com.tinqinacademy.hotel.api.operations.system.deleteroom.DeleteRoomOutput;
import com.tinqinacademy.hotel.api.operations.system.deleteroom.DeleteRoomOperation;
import com.tinqinacademy.hotel.core.errors.ErrorMapper;
import com.tinqinacademy.hotel.api.exception.exceptions.DeleteRoomException;
import com.tinqinacademy.hotel.api.exception.exceptions.NotFoundException;
import com.tinqinacademy.hotel.core.processors.BaseOperationProcessor;
import com.tinqinacademy.hotel.persistence.model.Room;
import com.tinqinacademy.hotel.persistence.repository.BookingRepository;
import com.tinqinacademy.hotel.persistence.repository.RoomRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@Slf4j
public class DeleteRoomOperationProcessor extends BaseOperationProcessor implements DeleteRoomOperation {
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;

    public DeleteRoomOperationProcessor(ConversionService conversionService, ErrorMapper errorMapper,
                                        Validator validator, RoomRepository roomRepository,
                                        BookingRepository bookingRepository) {
        super(conversionService, errorMapper, validator);
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
    }


    private Room getRoom(String id) {
        return roomRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new NotFoundException("Room with id:" + id));
    }

    private void checkRoomOccupied(Room room) {
        LocalDate today = LocalDate.now();
        if (bookingRepository.checkRoomOccupied(room.getId(), today, today)) {
            throw new DeleteRoomException(room.getId().toString());
        }
    }


    @Override
    public Either<Errors, DeleteRoomOutput> process(DeleteRoomInput input) {
        return Try.of(() -> {
                    log.info("Start deleteRoom input:{}", input);
                    validate(input);
                    Room room = getRoom(input.getId());
                    checkRoomOccupied(room);

                    roomRepository.delete(room);

                    DeleteRoomOutput result = DeleteRoomOutput.builder()
                            .build();
                    log.info("End deleteRoom result:{}", result);

                    return result;
                })
                .toEither()
                .mapLeft(errorMapper::map);
    }
}
