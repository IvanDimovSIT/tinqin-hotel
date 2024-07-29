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
    @Size(min = 2, max = 50, message = "Between 2 and 50 characters")
    private String firstName;
    @Size(min = 2, max = 50, message = "Between 2 and 50 characters")
    private String lastName;
    @Pattern(regexp = "$[0-9]{10}", message = "10 Digits")
    private String phoneNumber;
    @Pattern(regexp = "$[0-9]{8,16}", message = "8-16 Digits")
    private String idCardNumber;
    private LocalDate idCardValidity;
    private String idCardIssueAuthority;
    private LocalDate idCardIssueDate;
    @Pattern(regexp = "[0-9]{1,10}[A-Z]?")
    private String roomNumber;
}
