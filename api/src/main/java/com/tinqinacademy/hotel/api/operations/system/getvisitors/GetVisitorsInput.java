package com.tinqinacademy.hotel.api.operations.system.getvisitors;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class GetVisitorsInput {
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
    @Pattern(regexp = "$[0-9]{10}", message = "10 Digits")
    private String phoneNumber;
    @NotEmpty
    @Pattern(regexp = "$[0-9]{8,16}", message = "8-16 Digits")
    private String idCardNumber;
    @NotNull
    private LocalDate idCardValidity;
    @NotEmpty
    private String idCardIssueAuthority;
    @NotNull
    private LocalDate idCardIssueDate;
    @NotEmpty
    @Pattern(regexp = "[0-9]{1,10}[A-Z]?")
    private String roomNumber;
}
