package com.tinqinacademy.hotel.core.processors.system;

import com.tinqinacademy.hotel.api.errors.Errors;
import com.tinqinacademy.hotel.api.operations.system.updateroom.UpdateRoomInput;
import com.tinqinacademy.hotel.api.operations.system.updateroom.UpdateRoomOutput;
import com.tinqinacademy.hotel.api.operations.system.updateroom.UpdateRoomOperation;
import com.tinqinacademy.hotel.core.errors.ErrorMapper;
import com.tinqinacademy.hotel.core.exception.exceptions.NotFoundException;
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

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class UpdateRoomOperationProcessor extends BaseOperationProcessor implements UpdateRoomOperation {
    private final RoomRepository roomRepository;
    private final BedRepository bedRepository;

    public UpdateRoomOperationProcessor(ConversionService conversionService, ErrorMapper errorMapper, Validator validator, RoomRepository roomRepository, BedRepository bedRepository) {
        super(conversionService, errorMapper, validator);
        this.roomRepository = roomRepository;
        this.bedRepository = bedRepository;
    }


    Room getRoom(String id) {
        return roomRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new NotFoundException("Room with id:" + id));
    }

    List<Bed> findBedToAdd(BedSize bedSize, Integer bedCount) {
        Bed bedToAdd = bedRepository.findByBedSize(bedSize).orElseThrow(
                () -> new NotFoundException("BedSize: " + bedSize.toString()));
        List<Bed> beds = IntStream.range(0, bedCount).mapToObj(i -> bedToAdd).collect(Collectors.toList());

        return beds;
    }

    @Override
    public Either<Errors, UpdateRoomOutput> process(UpdateRoomInput input) {
        log.info("Start updateRoom input:{}", input);

        Either<Errors, UpdateRoomOutput> result = Try.of(() -> {
                    validate(input);
                    Room room = getRoom(input.getRoomId());

                    List<Bed> beds = findBedToAdd(BedSize.getCode(input.getBedSize().toString()), input.getBedCount());

                    room.setRoomNo(input.getRoomNumber());
                    room.setPrice(input.getPrice());
                    room.setFloor(input.getFloor());
                    room.setBathroomType(BathroomType.getCode(input.getBathroomType().toString()));
                    room.setBeds(beds);

                    room = roomRepository.save(room);

                    return conversionService.convert(room, UpdateRoomOutput.class);
                })
                .toEither()
                .mapLeft(errorMapper::map);

        log.info("End updateRoom result:{}", result);

        return result;
    }
}
