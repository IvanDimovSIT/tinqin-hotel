package com.tinqinacademy.hotel.core.processors.system;

import com.tinqinacademy.hotel.api.errors.Errors;
import com.tinqinacademy.hotel.api.operations.system.addroom.AddRoomInput;
import com.tinqinacademy.hotel.api.operations.system.addroom.AddRoomOutput;
import com.tinqinacademy.hotel.api.operations.system.addroom.AddRoomOperation;
import com.tinqinacademy.hotel.core.errors.ErrorMapper;
import com.tinqinacademy.hotel.core.exception.exceptions.CreateRoomException;
import com.tinqinacademy.hotel.core.exception.exceptions.NotFoundException;
import com.tinqinacademy.hotel.core.processors.BaseOperationProcessor;
import com.tinqinacademy.hotel.persistence.model.Bed;
import com.tinqinacademy.hotel.persistence.model.Room;
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
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class AddRoomOperationProcessor extends BaseOperationProcessor implements AddRoomOperation {
    private final BedRepository bedRepository;
    private final RoomRepository roomRepository;

    public AddRoomOperationProcessor(ConversionService conversionService, ErrorMapper errorMapper, Validator validator,
                                     BedRepository bedRepository, RoomRepository roomRepository) {
        super(conversionService, errorMapper, validator);
        this.bedRepository = bedRepository;
        this.roomRepository = roomRepository;
    }


    private List<Bed> findBedsToAdd(BedSize bedSize, Integer bedCount){
        Optional<Bed> bed = bedRepository.findByBedSize(bedSize);
        if (bed.isEmpty()) {
            throw new NotFoundException("Beds with type " + bedSize + " not found");
        }


        List<Bed> bedsToAdd = IntStream.range(0, bedCount).mapToObj(i -> bed.get())
                .collect(Collectors.toList());

        return bedsToAdd;
    }

    private void checkRoomWithNumberExists(AddRoomInput input) {
        if (roomRepository.findByRoomNo(input.getRoomNumber()).isPresent()) {
            throw new CreateRoomException("Room with number " + input.getRoomNumber() + " already exists");
        }
    }

    @Override
    public Either<Errors, AddRoomOutput> process(AddRoomInput input) {
        log.info("Start addRoom input:{}", input);

        Either<Errors, AddRoomOutput> result = Try.of(() -> {
                    validate(input);
                    checkRoomWithNumberExists(input);

                    List<Bed> bedsToAdd = findBedsToAdd(BedSize.getCode(input.getBedSize().toString()), input.getBedCount());

                    Room room = conversionService.convert(input, Room.class);
                    room.setBeds(bedsToAdd);

                    room = roomRepository.save(room);

                    return conversionService.convert(room, AddRoomOutput.class);
                })
                .toEither()
                .mapLeft(errorMapper::map);
        log.info("End addRoom result:{}", result);

        return result;
    }
}
