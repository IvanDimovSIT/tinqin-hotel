package com.tinqinacademy.hotel.api.errors;

import lombok.*;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString
@Getter
@Setter
public class ErrorInfo {
    private String error;
    private HttpStatus status;
}
