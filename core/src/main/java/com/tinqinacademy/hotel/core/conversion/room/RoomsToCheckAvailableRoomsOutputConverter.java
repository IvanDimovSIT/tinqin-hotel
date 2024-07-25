package com.tinqinacademy.hotel.core.conversion.room;

import com.tinqinacademy.hotel.api.operations.hotel.checkavailablerooms.CheckAvailableRoomsOutput;
import com.tinqinacademy.hotel.core.conversion.BaseConverter;
import com.tinqinacademy.hotel.persistence.model.Room;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoomsToCheckAvailableRoomsOutputConverter extends BaseConverter<List<Room>, CheckAvailableRoomsOutput> {
    @Override
    public CheckAvailableRoomsOutput convertObject(List<Room> source) {
        CheckAvailableRoomsOutput output = CheckAvailableRoomsOutput.builder()
                .ids(source.stream()
                        .map(room -> room.getId().toString())
                        .toList())
                .build();

        return output;
    }
}
