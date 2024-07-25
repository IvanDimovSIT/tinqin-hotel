package com.tinqinacademy.hotel.api.operations.hotel.getroom;


import com.tinqinacademy.hotel.api.model.enums.BathroomType;
import com.tinqinacademy.hotel.api.model.enums.BedSize;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class GetRoomOutput {
    private String id;
    private String number;
    private Integer bedCount;
    private BedSize bedSize;
    private Integer floor;
    private BigDecimal price;
    private BathroomType bathroomType;
    private List<LocalDate> datesOccupied;
}
