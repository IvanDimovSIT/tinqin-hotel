package com.tinqinacademy.hotel.core.services.hotel;

import com.tinqinacademy.hotel.api.operations.hotel.checkavailablerooms.CheckAvailableRoomsInput;
import com.tinqinacademy.hotel.api.operations.hotel.checkavailablerooms.CheckAvailableRoomsOutput;
import com.tinqinacademy.hotel.api.operations.hotel.checkavailablerooms.CheckAvailableRoomsService;
import com.tinqinacademy.hotel.persistence.model.Room;
import com.tinqinacademy.hotel.persistence.model.enums.BathroomType;
import com.tinqinacademy.hotel.persistence.model.enums.BedSize;
import com.tinqinacademy.hotel.persistence.repository.BedRepository;
import com.tinqinacademy.hotel.persistence.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CheckAvailableRoomsServiceImpl implements CheckAvailableRoomsService {
    private final RoomRepository roomRepository;
    private final ConversionService conversionService;

    @Override
    public CheckAvailableRoomsOutput process(CheckAvailableRoomsInput input) {
        log.info("Start checkAvailableRooms input:{}", input);

        BathroomType bathroomTypeCriteria = input.getBathroomType() == null ? null :
                com.tinqinacademy.hotel.persistence.model.enums.BathroomType
                        .getCode(input.getBathroomType().toString());

        BedSize bedSizeCriteria = input.getBedSize() == null ? null :
                com.tinqinacademy.hotel.persistence.model.enums.BedSize
                        .getCode(input.getBedSize().toString());

        List<Room> rooms = roomRepository.findAvailableRooms(
                input.getStartDate(),
                input.getEndDate(),
                bathroomTypeCriteria,
                bedSizeCriteria,
                input.getBedCount());

        CheckAvailableRoomsOutput result = conversionService.convert(rooms, CheckAvailableRoomsOutput.class);
        log.info("End checkAvailableRooms result:{}", result);

        return result;
    }
}
