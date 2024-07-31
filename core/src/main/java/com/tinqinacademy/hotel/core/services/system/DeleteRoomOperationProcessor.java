package com.tinqinacademy.hotel.core.services.system;

import com.tinqinacademy.hotel.api.errors.Errors;
import com.tinqinacademy.hotel.api.operations.system.deleteroom.DeleteRoomInput;
import com.tinqinacademy.hotel.api.operations.system.deleteroom.DeleteRoomOutput;
import com.tinqinacademy.hotel.api.operations.system.deleteroom.DeleteRoomOperation;
import com.tinqinacademy.hotel.core.errors.ErrorMapper;
import com.tinqinacademy.hotel.core.exception.exceptions.DeleteRoomException;
import com.tinqinacademy.hotel.core.exception.exceptions.NotFoundException;
import com.tinqinacademy.hotel.persistence.model.Room;
import com.tinqinacademy.hotel.persistence.repository.BookingRepository;
import com.tinqinacademy.hotel.persistence.repository.RoomRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;
import java.util.logging.ErrorManager;

import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeleteRoomOperationProcessor implements DeleteRoomOperation {
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final ErrorMapper errorMapper;

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
        log.info("Start deleteRoom input:{}", input);

        Either<Errors, DeleteRoomOutput> result = Try.of(() -> {
                    Room room = getRoom(input.getId());
                    checkRoomOccupied(room);

                    roomRepository.delete(room);

                    return DeleteRoomOutput.builder()
                            .build();
                })
                .toEither()
                .mapLeft(errorMapper::map);

        log.info("End deleteRoom result:{}", result);

        return result;
    }
}
