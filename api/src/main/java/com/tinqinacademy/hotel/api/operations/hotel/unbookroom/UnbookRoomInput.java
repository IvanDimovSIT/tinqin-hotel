package com.tinqinacademy.hotel.api.operations.hotel.unbookroom;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tinqinacademy.hotel.api.base.OperationInput;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.validator.constraints.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString
@Getter
@Setter
public class UnbookRoomInput implements OperationInput {
    @JsonIgnore
    @UUID
    @NotEmpty
    private String bookingId;
    @NotEmpty
    @UUID
    private String userId;
}
