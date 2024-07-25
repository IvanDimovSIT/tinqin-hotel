package com.tinqinacademy.hotel.api.operations.hotel.bookroom;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString
@Getter
@Setter
public class BookRoomInput {
    @JsonIgnore
    private String id;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
    @NotEmpty
    @Size(min = 2, max = 50, message = "Between 2 and 50 characters")
    private String firstName;
    @NotEmpty
    @Size(min = 2, max = 50, message = "Between 2 and 50 characters")
    private String lastName;
    @NotEmpty
    @Pattern(regexp = "[0-9]{10}", message = "10 Digits")
    private String phoneNumber;
}
