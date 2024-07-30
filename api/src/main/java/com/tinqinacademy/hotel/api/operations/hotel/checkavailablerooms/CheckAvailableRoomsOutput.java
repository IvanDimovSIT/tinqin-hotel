package com.tinqinacademy.hotel.api.operations.hotel.checkavailablerooms;

import com.tinqinacademy.hotel.api.base.OperationOutput;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class CheckAvailableRoomsOutput implements OperationOutput {
    private List<String> ids;
}
