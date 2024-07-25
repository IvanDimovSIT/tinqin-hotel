package com.tinqinacademy.hotel.core.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString
@Getter
@Setter
public class ErrorWrapper {
    private List<ErrorInfo> errors;
    private HttpStatus httpStatus;

    public void add(ErrorInfo error) {
        if(errors == null) {
            errors = new ArrayList<>();
        }

        errors.add(error);
        if(httpStatus == null || httpStatus.compareTo(error.getHttpStatus()) < 0) {
            httpStatus = error.getHttpStatus();
        }
    }
}
