package com.tinqinacademy.hotel.core.conversion.room;

import com.tinqinacademy.hotel.api.model.enums.BathroomType;
import com.tinqinacademy.hotel.api.model.enums.BedSize;
import com.tinqinacademy.hotel.api.operations.hotel.getroom.GetRoomOutput;
import com.tinqinacademy.hotel.core.conversion.BaseConverter;
import com.tinqinacademy.hotel.persistence.model.Room;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RoomToGetRoomOutputConverter extends BaseConverter<Room, GetRoomOutput> {
    @Override
    public GetRoomOutput convertObject(Room source) {
        GetRoomOutput output = GetRoomOutput.builder()
                .id(source.getId().toString())
                .bedSize(BedSize.getCode(source.getBeds().getFirst().getBedSize().toString()))
                .bedCount(source.getBeds().size())
                .bathroomType(BathroomType.getCode(source.getBathroomType().toString()))
                .floor(source.getFloor())
                .price(source.getPrice())
                .number(source.getRoomNo())
                .build();

        return output;
    }
}
