package com.tinqinacademy.hotel.core.errors;

import com.tinqinacademy.hotel.api.errors.Errors;

public interface ErrorMapper {
    Errors map(Throwable throwable);
}
