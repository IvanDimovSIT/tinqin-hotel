package com.tinqinacademy.hotel.core.processors.system;

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
import com.tinqinacademy.hotel.core.exception.exceptions.NotFoundException;
import com.tinqinacademy.hotel.core.exception.exceptions.PartialUpdateRoomException;
import com.tinqinacademy.hotel.core.processors.BaseOperationProcessor;
import com.tinqinacademy.hotel.persistence.model.Bed;
import com.tinqinacademy.hotel.persistence.model.Room;
import com.tinqinacademy.hotel.persistence.model.enums.BathroomType;
import com.tinqinacademy.hotel.persistence.model.enums.BedSize;
import com.tinqinacademy.hotel.persistence.repository.BedRepository;
import com.tinqinacademy.hotel.persistence.repository.RoomRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class PartialUpdateRoomOperationProcessor extends BaseOperationProcessor implements PartialUpdateRoomOperation {
    private final RoomRepository roomRepository;
    private final BedRepository bedRepository;
    private final ObjectMapper objectMapper;

    public PartialUpdateRoomOperationProcessor(ConversionService conversionService, ErrorMapper errorMapper,
                                               Validator validator, RoomRepository roomRepository,
                                               BedRepository bedRepository, ObjectMapper objectMapper) {
        super(conversionService, errorMapper, validator);
        this.roomRepository = roomRepository;
        this.bedRepository = bedRepository;
        this.objectMapper = objectMapper;
    }


    private Room getRoom(String id) {
        return roomRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new NotFoundException("Room with id:" + id));
    }

    private List<Bed> findBedsToAdd(Room currentRoom, Optional<BedSize> bedSize, Optional<Integer> bedCount) {
        List<Bed> newBeds = new ArrayList<>();
        Bed bedToAdd = currentRoom.getBeds().getFirst();
        int numberOfbedsToAdd = currentRoom.getBeds().size();
        if (bedSize.isPresent()) {
            bedToAdd = bedRepository.findByBedSize(BedSize.getCode(bedSize.get().toString())).get();
        }
        if (bedCount.isPresent()) {
            numberOfbedsToAdd = bedCount.get();
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

    private Optional<BedSize> convertToBedSize(PartialUpdateRoomInput input) {
        Optional<BedSize> bedSize = input.getBedSize() == null? Optional.empty():
                Optional.of(BedSize.getCode(input.getBedSize().toString()));

        return bedSize;
    }

    @Override
    public Either<Errors, PartialUpdateRoomOutput> process(PartialUpdateRoomInput input) {
        return Try.of(() -> {
                    log.info("Start partialUpdateRoom input:{}", input);
                    validate(input);
                    Room currentRoom = getRoom(input.getRoomId());

                    List<Bed> newBeds = findBedsToAdd(
                            currentRoom,
                            convertToBedSize(input),
                            Optional.ofNullable(input.getBedCount()));

                    Room newRoom = conversionService.convert(input, Room.class);

                    newRoom.setBeds(newBeds);

                    newRoom = patchRoom(currentRoom, newRoom);

                    Room savedRoom = roomRepository.save(newRoom);

                    PartialUpdateRoomOutput result = conversionService.convert(savedRoom, PartialUpdateRoomOutput.class);
                    log.info("End partialUpdateRoom result:{}", result);
                    return result;
                })
                .toEither()
                .mapLeft(errorMapper::map);
    }
}
