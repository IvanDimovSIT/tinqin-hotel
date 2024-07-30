package com.tinqinacademy.hotel.api.operations.system.updateroom;

import com.tinqinacademy.hotel.api.base.OperationOutput;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class UpdateRoomOutput implements OperationOutput {
    private String id;
}
