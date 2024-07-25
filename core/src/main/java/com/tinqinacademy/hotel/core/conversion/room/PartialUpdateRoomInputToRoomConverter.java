package com.tinqinacademy.hotel.core.conversion.room;

import com.tinqinacademy.hotel.api.operations.system.partialupdateroom.PartialUpdateRoomInput;
import com.tinqinacademy.hotel.core.conversion.BaseConverter;
import com.tinqinacademy.hotel.persistence.model.Room;
import com.tinqinacademy.hotel.persistence.model.enums.BathroomType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PartialUpdateRoomInputToRoomConverter extends BaseConverter<PartialUpdateRoomInput, Room> {

    @Override
    public Room convertObject(PartialUpdateRoomInput source) {
        Room room = Room.builder()
                .id(UUID.fromString(source.getRoomId()))
                .roomNo(source.getRoomNumber())
                .floor(source.getFloor())
                .price(source.getPrice())
                .bathroomType(
                        source.getBathroomType() == null? null :
                                BathroomType.getCode(source.getBathroomType().toString())
                )
                .build();

        return room;
    }
}
