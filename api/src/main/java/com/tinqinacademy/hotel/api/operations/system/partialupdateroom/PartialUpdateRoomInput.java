package com.tinqinacademy.hotel.api.operations.system.partialupdateroom;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tinqinacademy.hotel.api.base.OperationInput;
import com.tinqinacademy.hotel.api.model.enums.BathroomType;
import com.tinqinacademy.hotel.api.model.enums.BedSize;
import jakarta.validation.constraints.*;
import lombok.*;

import javax.swing.text.html.Option;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString
@Getter
@Setter
public class PartialUpdateRoomInput implements OperationInput {
    @JsonIgnore
    private String roomId;
    @Min(value = 1)
    @Max(value = 10)
    private Integer bedCount;
    private BedSize bedSize;
    private BathroomType bathroomType;
    @Min(value = 1)
    @Max(value = 20)
    private Integer floor;
    @Pattern(regexp = "[0-9]{1,10}[A-Z]?")
    private String roomNumber;
    @Positive
    private BigDecimal price;
}
