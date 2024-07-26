package com.tinqinacademy.hotel.core.conversion.room;

import com.tinqinacademy.hotel.api.operations.system.addroom.AddRoomInput;
import com.tinqinacademy.hotel.core.conversion.BaseConverter;
import com.tinqinacademy.hotel.persistence.model.Room;
import com.tinqinacademy.hotel.persistence.model.enums.BathroomType;
import org.springframework.stereotype.Component;

@Component
public class AddRoomInputToRoomConverter extends BaseConverter<AddRoomInput, Room> {
    @Override
    protected Room convertObject(AddRoomInput source) {
        Room room = Room.builder()
                .roomNo(source.getRoomNumber())
                .floor(source.getFloor())
                .price(source.getPrice())
                .bathroomType(BathroomType.getCode(source.getBathroomType().toString()))
                .build();

        return room;
    }
}
