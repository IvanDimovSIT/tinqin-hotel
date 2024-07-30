package com.tinqinacademy.hotel.api.errors;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@ToString
@AllArgsConstructor
public class Errors extends RuntimeException {
    public static class ErrorsBuilder {
        private List<ErrorInfo> errorInfos;

        public ErrorsBuilder() {
            errorInfos = new ArrayList<>();
        }

        public ErrorsBuilder error(String message, HttpStatus status) {
            errorInfos.add(
                    ErrorInfo.builder()
                            .error(message)
                            .status(status)
                            .build()
            );

            return this;
        }

        public Errors build() {
            return new Errors(errorInfos);
        }
    }

    private final List<ErrorInfo> errorInfos;

    public void addError(ErrorInfo errorInfo) {
        errorInfos.add(errorInfo);
    }

    public static ErrorsBuilder builder() {
        return new ErrorsBuilder();
    }
}
