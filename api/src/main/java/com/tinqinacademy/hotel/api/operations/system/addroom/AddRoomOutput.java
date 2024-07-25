package com.tinqinacademy.hotel.api.operations.system.addroom;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class AddRoomOutput {
    private String id;
}
