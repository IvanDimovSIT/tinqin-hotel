package com.tinqinacademy.hotel.api.operations.system.getvisitors;

import com.tinqinacademy.hotel.api.model.visitor.VisitorOutput;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class GetVisitorsOutput {
    private List<VisitorOutput> visitorOutputs;
}
