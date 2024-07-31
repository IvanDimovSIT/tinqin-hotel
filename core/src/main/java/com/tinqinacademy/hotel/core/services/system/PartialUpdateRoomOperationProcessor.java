package com.tinqinacademy.hotel.core.services.system;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.tinqinacademy.hotel.api.errors.Errors;
import com.tinqinacademy.hotel.api.operations.system.partialupdateroom.PartialUpdateRoomInput;
import com.tinqinacademy.hotel.api.operations.system.partialupdateroom.PartialUpdateRoomOutput;
import com.tinqinacademy.hotel.api.operations.system.partialupdateroom.PartialUpdateRoomOperation;
import com.tinqinacademy.hotel.core.errors.ErrorMapper;
import com.tinqinacademy.hotel.core.exception.exceptions.DeleteRoomException;
import com.tinqinacademy.hotel.core.exception.exceptions.NotFoundException;
import com.tinqinacademy.hotel.core.exception.exceptions.PartialUpdateRoomException;
import com.tinqinacademy.hotel.persistence.model.Bed;
import com.tinqinacademy.hotel.persistence.model.Room;
import com.tinqinacademy.hotel.persistence.model.enums.BedSize;
import com.tinqinacademy.hotel.persistence.repository.BedRepository;
import com.tinqinacademy.hotel.persistence.repository.RoomRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;

@Service
@Slf4j
@RequiredArgsConstructor
public class PartialUpdateRoomOperationProcessor implements PartialUpdateRoomOperation {
    private final RoomRepository roomRepository;
    private final BedRepository bedRepository;
    private final ObjectMapper objectMapper;
    private final ConversionService conversionService;
    private final ErrorMapper errorMapper;

    private Room getRoom(String id) {
        return roomRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new NotFoundException("Room with id:" + id));
    }

    private List<Bed> findBedsToAdd(Room currentRoom, BedSize bedSize, Integer bedCount) {
        List<Bed> newBeds = new ArrayList<>();
        Bed bedToAdd = currentRoom.getBeds().getFirst();
        int numberOfbedsToAdd = currentRoom.getBeds().size();
        if (bedSize != null) {
            bedToAdd = bedRepository.findByBedSize(BedSize.getCode(bedSize.toString())).get();
        }
        if (bedCount != null) {
            numberOfbedsToAdd = bedCount;
        }
        for (int i = 0; i < numberOfbedsToAdd; i++) {
            newBeds.add(bedToAdd);
        }

        return newBeds;
    }

    private Room patchRoom(Room currentRoom, Room newRoom) {
        JsonNode roomNode = objectMapper.valueToTree(currentRoom);
        JsonNode inputNode = objectMapper.valueToTree(newRoom);

        try {
            JsonMergePatch patch = JsonMergePatch.fromJson(inputNode);
            return objectMapper.treeToValue(patch.apply(roomNode), Room.class);
        } catch (JsonPatchException | JsonProcessingException e) {
            throw new PartialUpdateRoomException(e.getMessage());
        }
    }

    @Override
    public Either<Errors, PartialUpdateRoomOutput> process(PartialUpdateRoomInput input) {
        log.info("Start partialUpdateRoom input:{}", input);

        Either<Errors, PartialUpdateRoomOutput> result = Try.of(() -> {
                    Room currentRoom = getRoom(input.getRoomId());

                    List<Bed> newBeds = findBedsToAdd(currentRoom, BedSize.getCode(input.getBedSize().toString()), input.getBedCount());

                    Room newRoom = conversionService.convert(input, Room.class);
                    newRoom.setBeds(newBeds);


                    newRoom = patchRoom(currentRoom, newRoom);

                    Room savedRoom = roomRepository.save(newRoom);

                    return conversionService.convert(savedRoom, PartialUpdateRoomOutput.class);
                })
                .toEither()
                .mapLeft(errorMapper::map);

        log.info("End partialUpdateRoom result:{}", result);

        return result;
    }
}
