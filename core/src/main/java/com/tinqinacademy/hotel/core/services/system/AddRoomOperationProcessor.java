package com.tinqinacademy.hotel.core.services.system;

import com.tinqinacademy.hotel.api.errors.Errors;
import com.tinqinacademy.hotel.api.operations.system.addroom.AddRoomInput;
import com.tinqinacademy.hotel.api.operations.system.addroom.AddRoomOutput;
import com.tinqinacademy.hotel.api.operations.system.addroom.AddRoomOperation;
import com.tinqinacademy.hotel.core.exception.exceptions.CreateRoomException;
import com.tinqinacademy.hotel.core.exception.exceptions.NotFoundException;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;

@Service
@Slf4j
@RequiredArgsConstructor
public class AddRoomOperationProcessor implements AddRoomOperation {
    private final BedRepository bedRepository;
    private final RoomRepository roomRepository;
    private final ConversionService conversionService;

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
                    checkRoomWithNumberExists(input);

                    List<Bed> bedsToAdd = findBedsToAdd(BedSize.getCode(input.getBedSize().toString()), input.getBedCount());

                    Room room = conversionService.convert(input, Room.class);
                    room.setBeds(bedsToAdd);

                    room = roomRepository.save(room);

                    return conversionService.convert(room, AddRoomOutput.class);
                })
                .toEither()
                .mapLeft(throwable -> Match(throwable).of(
                        Case($(instanceOf(NotFoundException.class)), Errors.builder()
                                .error(throwable.getMessage(), HttpStatus.NOT_FOUND)
                                .build()
                        ),
                        Case($(instanceOf(CreateRoomException.class)), Errors.builder()
                                .error(throwable.getMessage(), HttpStatus.BAD_REQUEST)
                                .build()
                        ),
                        Case($(), Errors.builder()
                                .error(throwable.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR)
                                .build()
                        )
                ));
        log.info("End addRoom result:{}", result);

        return result;
    }
}
