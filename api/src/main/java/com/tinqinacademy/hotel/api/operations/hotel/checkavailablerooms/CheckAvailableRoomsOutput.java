package com.tinqinacademy.hotel.api.operations.hotel.checkavailablerooms;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class CheckAvailableRoomsOutput {
    private List<String> ids;
}
