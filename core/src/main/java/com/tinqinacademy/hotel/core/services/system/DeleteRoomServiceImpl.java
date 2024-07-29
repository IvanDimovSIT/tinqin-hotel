package com.tinqinacademy.hotel.core.services.system;

import com.tinqinacademy.hotel.api.operations.system.deleteroom.DeleteRoomInput;
import com.tinqinacademy.hotel.api.operations.system.deleteroom.DeleteRoomOutput;
import com.tinqinacademy.hotel.api.operations.system.deleteroom.DeleteRoomService;
import com.tinqinacademy.hotel.core.exception.exceptions.DeleteRoomException;
import com.tinqinacademy.hotel.core.exception.exceptions.NotFoundException;
import com.tinqinacademy.hotel.persistence.model.Room;
import com.tinqinacademy.hotel.persistence.repository.BookingRepository;
import com.tinqinacademy.hotel.persistence.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeleteRoomServiceImpl implements DeleteRoomService {
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;

    private Room getRoom(String id){
        return roomRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new NotFoundException("Room with id:" + id + " not found"));
    }

    private boolean checkRoomOccupied(Room room) {
        LocalDate today = LocalDate.now();
        boolean isRoomOccupied = bookingRepository.checkRoomOccupied(room.getId(), today, today);

        return isRoomOccupied;
    }


    @Override
    public DeleteRoomOutput process(DeleteRoomInput input) {
        log.info("Start deleteRoom input:{}", input);

        Room room = getRoom(input.getId());

        boolean isRoomOccupied = checkRoomOccupied(room);

        if (isRoomOccupied) {
            throw new DeleteRoomException("Room with id:" + room.getId() + " is occupied");
        }

        roomRepository.delete(room);

        DeleteRoomOutput result = DeleteRoomOutput.builder()
                .build();

        log.info("End deleteRoom result:{}", result);

        return result;
    }
}
