package com.tinqinacademy.hotel.api.operations.system.getvisitors;

import com.tinqinacademy.hotel.api.operations.hotel.getroom.GetRoomInput;
import com.tinqinacademy.hotel.api.operations.hotel.getroom.GetRoomOutput;

public interface GetVisitorsService {
    GetVisitorsOutput process(GetVisitorsInput input);
}
