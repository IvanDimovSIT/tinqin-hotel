package com.tinqinacademy.hotel.api.operations.hotel.getroom;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class GetRoomInput {
    @NotEmpty
    private String id;
}
