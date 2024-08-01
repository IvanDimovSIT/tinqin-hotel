package com.tinqinacademy.hotel.core.processors.system;

import com.tinqinacademy.hotel.api.errors.Errors;
import com.tinqinacademy.hotel.api.model.enums.BathroomType;
import com.tinqinacademy.hotel.api.operations.system.addroom.AddRoomInput;
import com.tinqinacademy.hotel.api.operations.system.addroom.AddRoomOutput;
import com.tinqinacademy.hotel.api.operations.system.addroom.AddRoomOperation;
import com.tinqinacademy.hotel.core.errors.ErrorMapper;
import com.tinqinacademy.hotel.core.exception.exceptions.CreateRoomException;
import com.tinqinacademy.hotel.core.exception.exceptions.InvalidBathroomTypeException;
import com.tinqinacademy.hotel.core.exception.exceptions.InvalidBedSizeException;
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


    private List<Bed> findBedsToAdd(BedSize bedSize, Integer bedCount) {
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

    private BedSize convertBedSize(AddRoomInput input) {
        BedSize bedSize = BedSize.getCode(input.getBedSize().toString());
        if (bedSize == BedSize.UNKNOWN) {
            throw new InvalidBedSizeException(input.getBedSize().toString());
        }

        return bedSize;
    }

    void validateBathroomType(AddRoomInput input) {
        if (input.getBathroomType() == BathroomType.UNKNOWN) {
            throw new InvalidBathroomTypeException(input.getBathroomType().toString());
        }
    }

    @Override
    public Either<Errors, AddRoomOutput> process(AddRoomInput input) {
        return Try.of(() -> {
                    log.info("Start addRoom input:{}", input);
                    validate(input);
                    validateBathroomType(input);
                    checkRoomWithNumberExists(input);

                    List<Bed> bedsToAdd = findBedsToAdd(convertBedSize(input), input.getBedCount());

                    Room room = conversionService.convert(input, Room.class);
                    room.setBeds(bedsToAdd);

                    room = roomRepository.save(room);
                    AddRoomOutput result = conversionService.convert(room, AddRoomOutput.class);
                    log.info("End addRoom result:{}", result);
                    return result;
                })
                .toEither()
                .mapLeft(errorMapper::map);
    }
}
