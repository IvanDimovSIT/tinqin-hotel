package com.tinqinacademy.hotel.api.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum BedSize {
    SINGLE("single"),
    DOUBLE("double"),
    SMALL_DOUBLE("smallDouble"),
    KING_SIZE("kingSize"),
    QUEEN_SIZE("queenSize"),
    UNKNOWN("");

    private final String code;

    BedSize(String code) {
        this.code = code;
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
