package com.tinqinacademy.hotel.api.operations.hotel.unbookroom;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class UnbookRoomInput {
    @NotEmpty
    private String bookingId;
}
