package com.tinqinacademy.hotel.persistence.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;

public enum BedSize {
    SINGLE("single", 1),
    DOUBLE("double", 2),
    SMALL_DOUBLE("smallDouble", 2),
    KING_SIZE("kingSize", 4),
    QUEEN_SIZE("queenSize", 3),
    UNKNOWN("", 0);

    private final String code;
    @Getter
    private final Integer capacity;

    BedSize(String code, Integer capacity) {
        this.code = code;
        this.capacity = capacity;
    }

    @JsonCreator
    public static BedSize getCode(String code) {
        return Arrays.stream(BedSize.values())
                .filter(bedSize -> bedSize.code.equals(code))
                .findFirst()
                .orElse(UNKNOWN);
    }

    @JsonValue
    public String toString() {
        return code;
    }
}
