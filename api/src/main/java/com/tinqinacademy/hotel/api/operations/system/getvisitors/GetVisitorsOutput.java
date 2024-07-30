package com.tinqinacademy.hotel.api.operations.system.getvisitors;

import com.tinqinacademy.hotel.api.base.OperationOutput;
import com.tinqinacademy.hotel.api.model.visitor.VisitorOutput;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class GetVisitorsOutput implements OperationOutput {
    private List<VisitorOutput> visitorOutputs;
}
