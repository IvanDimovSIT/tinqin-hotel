package com.tinqinacademy.hotel.core.services.system;

import com.tinqinacademy.hotel.api.errors.Errors;
import com.tinqinacademy.hotel.api.operations.system.updateroom.UpdateRoomInput;
import com.tinqinacademy.hotel.api.operations.system.updateroom.UpdateRoomOutput;
import com.tinqinacademy.hotel.api.operations.system.updateroom.UpdateRoomOperation;
import com.tinqinacademy.hotel.core.exception.exceptions.NotFoundException;
import com.tinqinacademy.hotel.persistence.model.Bed;
import com.tinqinacademy.hotel.persistence.model.Room;
import com.tinqinacademy.hotel.persistence.model.enums.BathroomType;
import com.tinqinacademy.hotel.persistence.model.enums.BedSize;
import com.tinqinacademy.hotel.persistence.repository.BedRepository;
import com.tinqinacademy.hotel.persistence.repository.RoomRepository;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class UpdateRoomOperationProcessor implements UpdateRoomOperation {
    private final RoomRepository roomRepository;
    private final BedRepository bedRepository;
    private final ConversionService conversionService;

    Room getRoom(String id){
       return roomRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new NotFoundException("Room with id:" + id));
    }

    List<Bed> findBedToAdd(BedSize bedSize, Integer bedCount){
        Bed bedToAdd = bedRepository.findByBedSize(bedSize).get();
        List<Bed> beds = IntStream.range(0, bedCount).mapToObj(i -> bedToAdd).collect(Collectors.toList());

        return beds;
    }

    @Override
    public Either<Errors, UpdateRoomOutput> process(UpdateRoomInput input) {
        log.info("Start updateRoom input:{}", input);

        Room room = getRoom(input.getRoomId());

        room.setRoomNo(input.getRoomNumber());
        room.setPrice(input.getPrice());
        room.setFloor(input.getFloor());
        room.setBathroomType(BathroomType.getCode(input.getBathroomType().toString()));

        List<Bed> beds = findBedToAdd(BedSize.getCode(input.getBedSize().toString()), input.getBedCount());
        room.setBeds(beds);

        room = roomRepository.save(room);

        UpdateRoomOutput result = conversionService.convert(room, UpdateRoomOutput.class);

        log.info("End updateRoom result:{}", result);

        return result;
    }
}
