package com.tinqinacademy.hotel.core.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString
@Getter
@Setter
public class ErrorInfo {
    private String field;
    private String message;
    private HttpStatus httpStatus;
}
