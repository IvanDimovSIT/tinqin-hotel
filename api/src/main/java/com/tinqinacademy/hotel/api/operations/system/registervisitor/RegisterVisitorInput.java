package com.tinqinacademy.hotel.api.operations.system.registervisitor;

import com.tinqinacademy.hotel.api.base.OperationInput;
import com.tinqinacademy.hotel.api.model.visitor.VisitorInput;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class RegisterVisitorInput implements OperationInput {
    @NotNull
    private List<VisitorInput> visitorInputs;
}
