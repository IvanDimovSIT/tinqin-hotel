package com.tinqinacademy.hotel.core.processors.hotel;

import com.tinqinacademy.hotel.api.errors.Errors;
import com.tinqinacademy.hotel.api.operations.hotel.checkavailablerooms.CheckAvailableRoomsInput;
import com.tinqinacademy.hotel.api.operations.hotel.checkavailablerooms.CheckAvailableRoomsOutput;
import com.tinqinacademy.hotel.api.operations.hotel.checkavailablerooms.CheckAvailableRoomsOperation;
import com.tinqinacademy.hotel.core.errors.ErrorMapper;
import com.tinqinacademy.hotel.core.exception.exceptions.InvalidBathroomTypeException;
import com.tinqinacademy.hotel.core.exception.exceptions.InvalidBedSizeException;
import com.tinqinacademy.hotel.core.processors.BaseOperationProcessor;
import com.tinqinacademy.hotel.persistence.model.Room;
import com.tinqinacademy.hotel.persistence.model.enums.BathroomType;
import com.tinqinacademy.hotel.persistence.model.enums.BedSize;
import com.tinqinacademy.hotel.persistence.repository.RoomRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CheckAvailableRoomsOperationProcessor extends BaseOperationProcessor implements CheckAvailableRoomsOperation {
    private final RoomRepository roomRepository;

    public CheckAvailableRoomsOperationProcessor(ConversionService conversionService, ErrorMapper errorMapper,
                                                 Validator validator, RoomRepository roomRepository) {
        super(conversionService, errorMapper, validator);
        this.roomRepository = roomRepository;
    }


    private BathroomType getBathroomTypeCriteria(CheckAvailableRoomsInput input) {
        BathroomType bathroomType = input.getBathroomType() == null ? null :
                BathroomType
                        .getCode(input.getBathroomType().toString());

        if(bathroomType == BathroomType.UNKNOWN) {
            throw new InvalidBathroomTypeException(input.getBathroomType().toString());
        }

        return bathroomType;
    }

    private BedSize getBedSizeCriteria(CheckAvailableRoomsInput input) {
        BedSize bedSize = input.getBedSize() == null ? null :
                BedSize
                        .getCode(input.getBedSize().toString());

        if(bedSize == BedSize.UNKNOWN) {
            throw new InvalidBedSizeException(bedSize.toString());
        }

        return bedSize;
    }

    @Override
    public Either<Errors, CheckAvailableRoomsOutput> process(CheckAvailableRoomsInput input) {
        return Try.of(() -> {
                    log.info("Start checkAvailableRooms input:{}", input);
                    validate(input);
                    BathroomType bathroomTypeCriteria = getBathroomTypeCriteria(input);
                    BedSize bedSizeCriteria = getBedSizeCriteria(input);

                    List<Room> rooms = roomRepository.findAvailableRooms(
                            input.getStartDate(),
                            input.getEndDate(),
                            bathroomTypeCriteria,
                            bedSizeCriteria,
                            input.getBedCount());

                    CheckAvailableRoomsOutput result = conversionService.convert(rooms, CheckAvailableRoomsOutput.class);
                    log.info("End checkAvailableRooms result:{}", result);
                    return result;
                })
                .toEither()
                .mapLeft(errorMapper::map);
    }
}
