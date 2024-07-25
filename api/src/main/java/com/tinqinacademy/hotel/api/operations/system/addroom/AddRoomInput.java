package com.tinqinacademy.hotel.api.operations.system.addroom;


import com.tinqinacademy.hotel.api.model.enums.BathroomType;
import com.tinqinacademy.hotel.api.model.enums.BedSize;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class AddRoomInput {
    @NotNull
    @Min(value = 1)
    @Max(value = 10)
    private Integer bedCount;
    @NotNull
    private BedSize bedSize;
    @NotNull
    private BathroomType bathroomType;
    @NotNull
    @Min(value = 1)
    @Max(value = 20)
    private Integer floor;
    @NotEmpty
    @Pattern(regexp = "[0-9]{1,10}[A-Z]?", message = "Valid room numbers start with numbers and could end with a letter")
    private String roomNumber;
    @Positive
    @NotNull
    private BigDecimal price;
}
