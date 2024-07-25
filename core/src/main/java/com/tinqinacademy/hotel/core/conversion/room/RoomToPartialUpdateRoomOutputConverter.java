package com.tinqinacademy.hotel.core.conversion.room;

import com.tinqinacademy.hotel.api.operations.system.partialupdateroom.PartialUpdateRoomOutput;
import com.tinqinacademy.hotel.api.operations.system.updateroom.UpdateRoomOutput;
import com.tinqinacademy.hotel.core.conversion.BaseConverter;
import com.tinqinacademy.hotel.persistence.model.Room;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RoomToPartialUpdateRoomOutputConverter extends BaseConverter<Room, PartialUpdateRoomOutput> {
    @Override
    public PartialUpdateRoomOutput convertObject(Room source) {
        PartialUpdateRoomOutput output = PartialUpdateRoomOutput.builder()
                .id(source.getId().toString())
                .build();

        return output;
    }
}
