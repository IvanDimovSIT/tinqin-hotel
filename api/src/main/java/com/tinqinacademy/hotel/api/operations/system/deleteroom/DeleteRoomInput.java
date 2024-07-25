package com.tinqinacademy.hotel.api.operations.system.deleteroom;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class DeleteRoomInput {
    @NotEmpty
    private String id;
}
