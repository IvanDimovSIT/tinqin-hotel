package com.tinqinacademy.hotel.api.operations.system.updateroom;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tinqinacademy.hotel.api.base.OperationInput;
import com.tinqinacademy.hotel.api.model.enums.BathroomType;
import com.tinqinacademy.hotel.api.model.enums.BedSize;
import com.tinqinacademy.hotel.api.validation.bathroomtype.ValidBathroomType;
import com.tinqinacademy.hotel.api.validation.bedsize.ValidBedSize;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString
@Getter
@Setter
public class UpdateRoomInput implements OperationInput {
    @JsonIgnore
    private String roomId;
    @NotNull
    @Min(value = 1)
    @Max(value = 10)
    private Integer bedCount;
    @NotNull
    @ValidBedSize
    private BedSize bedSize;
    @NotNull
    @ValidBathroomType
    private BathroomType bathroomType;
    @NotNull
    @Min(value = 1)
    @Max(value = 20)
    private Integer floor;
    @NotEmpty
    @Pattern(regexp = "[0-9]{1,10}[A-Z]?")
    private String roomNumber;
    @Positive
    @NotNull
    private BigDecimal price;
}
