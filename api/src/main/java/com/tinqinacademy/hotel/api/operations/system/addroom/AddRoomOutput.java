package com.tinqinacademy.hotel.api.operations.system.addroom;

import com.tinqinacademy.hotel.api.base.OperationOutput;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class AddRoomOutput implements OperationOutput {
    private String id;
}
