package com.tinqinacademy.hotel.core.conversion.room;

import com.tinqinacademy.hotel.api.operations.system.updateroom.UpdateRoomOutput;
import com.tinqinacademy.hotel.core.conversion.BaseConverter;
import com.tinqinacademy.hotel.persistence.model.Room;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RoomToUpdateRoomOutputConverter extends BaseConverter<Room, UpdateRoomOutput> {
    @Override
    public UpdateRoomOutput convertObject(Room source) {
        UpdateRoomOutput output = UpdateRoomOutput.builder()
                .id(source.getId().toString())
                .build();

        return output;
    }
}
