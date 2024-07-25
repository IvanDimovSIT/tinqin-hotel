package com.tinqinacademy.hotel.persistence.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum BathroomType {
    PRIVATE("private"),
    SHARED("shared"),
    UNKNOWN("");

    private final String code;

    BathroomType(String code){
        this.code = code;
    }

    @JsonCreator
    public static BathroomType getCode(String code) {
        return Arrays.stream(BathroomType.values())
                .filter(bathroomType -> bathroomType.code.equals(code))
                .findFirst()
                .orElse(UNKNOWN);
    }

    @JsonValue
    public String toString() {
        return code;
    }
}
