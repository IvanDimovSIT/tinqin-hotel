package com.tinqinacademy.hotel.core.conversion.room;

import com.tinqinacademy.hotel.api.operations.system.addroom.AddRoomOutput;
import com.tinqinacademy.hotel.core.conversion.BaseConverter;
import com.tinqinacademy.hotel.persistence.model.Room;
import org.springframework.stereotype.Component;

@Component
public class RoomToAddRoomOutputConverter extends BaseConverter<Room, AddRoomOutput> {
    @Override
    public AddRoomOutput convertObject(Room source) {
        AddRoomOutput output = AddRoomOutput.builder()
                .id(source.getId().toString())
                .build();

        return output;
    }
}
