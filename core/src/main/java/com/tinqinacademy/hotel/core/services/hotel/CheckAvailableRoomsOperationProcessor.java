package com.tinqinacademy.hotel.core.services.hotel;

import com.tinqinacademy.hotel.api.errors.Errors;
import com.tinqinacademy.hotel.api.operations.hotel.checkavailablerooms.CheckAvailableRoomsInput;
import com.tinqinacademy.hotel.api.operations.hotel.checkavailablerooms.CheckAvailableRoomsOutput;
import com.tinqinacademy.hotel.api.operations.hotel.checkavailablerooms.CheckAvailableRoomsOperation;
import com.tinqinacademy.hotel.core.exception.exceptions.NotFoundException;
import com.tinqinacademy.hotel.persistence.model.Room;
import com.tinqinacademy.hotel.persistence.model.enums.BathroomType;
import com.tinqinacademy.hotel.persistence.model.enums.BedSize;
import com.tinqinacademy.hotel.persistence.repository.RoomRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;

@Service
@Slf4j
@RequiredArgsConstructor
public class CheckAvailableRoomsOperationProcessor implements CheckAvailableRoomsOperation {
    private final RoomRepository roomRepository;
    private final ConversionService conversionService;

    private BathroomType getBathroomTypeCriteria(CheckAvailableRoomsInput input) {
        return input.getBathroomType() == null ? null :
                com.tinqinacademy.hotel.persistence.model.enums.BathroomType
                        .getCode(input.getBathroomType().toString());
    }

    private BedSize getBedSizeCriteria(CheckAvailableRoomsInput input) {
        return input.getBedSize() == null ? null :
                com.tinqinacademy.hotel.persistence.model.enums.BedSize
                        .getCode(input.getBedSize().toString());
    }

    @Override
    public Either<Errors, CheckAvailableRoomsOutput> process(CheckAvailableRoomsInput input) {
        log.info("Start checkAvailableRooms input:{}", input);

        Either<Errors, CheckAvailableRoomsOutput> result = Try.of(() -> {
                    BathroomType bathroomTypeCriteria = getBathroomTypeCriteria(input);
                    BedSize bedSizeCriteria = getBedSizeCriteria(input);

                    List<Room> rooms = roomRepository.findAvailableRooms(
                            input.getStartDate(),
                            input.getEndDate(),
                            bathroomTypeCriteria,
                            bedSizeCriteria,
                            input.getBedCount());

                    return conversionService.convert(rooms, CheckAvailableRoomsOutput.class);
                })
                .toEither()
                .mapLeft(throwable -> Match(throwable).of(
                        Case($(), Errors.builder()
                                .error(throwable.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR)
                                .build()
                        )
                ));
        log.info("End checkAvailableRooms result:{}", result);

        return result;
    }
}
