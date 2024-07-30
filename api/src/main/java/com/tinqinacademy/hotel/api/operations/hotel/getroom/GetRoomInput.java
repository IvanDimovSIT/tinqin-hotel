package com.tinqinacademy.hotel.api.operations.hotel.getroom;

import com.tinqinacademy.hotel.api.base.OperationInput;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class GetRoomInput implements OperationInput {
    @NotEmpty
    private String id;
}
