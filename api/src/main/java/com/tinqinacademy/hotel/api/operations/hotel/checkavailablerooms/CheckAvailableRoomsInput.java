package com.tinqinacademy.hotel.api.operations.hotel.checkavailablerooms;


import com.tinqinacademy.hotel.api.base.OperationInput;
import com.tinqinacademy.hotel.api.model.enums.BathroomType;
import com.tinqinacademy.hotel.api.model.enums.BedSize;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class CheckAvailableRoomsInput implements OperationInput {
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
    @NotNull
    @Min(1)
    @Max(10)
    private Integer bedCount;
    @NotNull
    private BedSize bedSize;
    @NotNull
    private BathroomType bathroomType;
}
