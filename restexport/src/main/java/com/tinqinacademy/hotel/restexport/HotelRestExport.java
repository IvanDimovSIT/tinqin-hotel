package com.tinqinacademy.hotel.restexport;


import com.tinqinacademy.hotel.api.operations.hotel.bookroom.BookRoomInput;
import com.tinqinacademy.hotel.api.operations.hotel.bookroom.BookRoomOutput;
import com.tinqinacademy.hotel.api.operations.hotel.checkavailablerooms.CheckAvailableRoomsOutput;
import com.tinqinacademy.hotel.api.operations.hotel.getroom.GetRoomOutput;
import com.tinqinacademy.hotel.api.operations.hotel.unbookroom.UnbookRoomInput;
import com.tinqinacademy.hotel.api.operations.hotel.unbookroom.UnbookRoomOutput;
import com.tinqinacademy.hotel.api.operations.system.addroom.AddRoomInput;
import com.tinqinacademy.hotel.api.operations.system.addroom.AddRoomOutput;
import com.tinqinacademy.hotel.api.operations.system.deleteroom.DeleteRoomOutput;
import com.tinqinacademy.hotel.api.operations.system.getvisitors.GetVisitorsOutput;
import com.tinqinacademy.hotel.api.operations.system.partialupdateroom.PartialUpdateRoomInput;
import com.tinqinacademy.hotel.api.operations.system.partialupdateroom.PartialUpdateRoomOutput;
import com.tinqinacademy.hotel.api.operations.system.registervisitor.RegisterVisitorInput;
import com.tinqinacademy.hotel.api.operations.system.registervisitor.RegisterVisitorOutput;
import com.tinqinacademy.hotel.api.operations.system.updateroom.UpdateRoomInput;
import com.tinqinacademy.hotel.api.operations.system.updateroom.UpdateRoomOutput;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


@Headers({"Content-Type: application/json"})
public interface HotelRestExport {

    @RequestLine("GET /api/v1/hotel/rooms?startDate={startDate}&endDate={endDate}&bedCount={bedCount}&bedSize={bedSize}&bathroomType={bathroomType}")
    CheckAvailableRoomsOutput checkAvailableRooms(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("bedCount") Integer bedCount,
            @Param("bedSize") String bedSize,
            @Param("bathroomType") String bathroomType);

    @RequestLine("GET /api/v1/hotel/{roomId}")
    GetRoomOutput getRoom(@Param("roomId") String roomId);

    @RequestLine("POST /api/v1/hotel/{roomId}")
    BookRoomOutput bookRoom(@Param("roomId") String roomId, @RequestBody BookRoomInput bookRoomInput);

    @RequestLine("DELETE /api/v1/hotel/{bookingId}")
    UnbookRoomOutput unbookRoom(@Param("bookingId") String bookingId, @RequestBody UnbookRoomInput unbookRoomInput);

    @RequestLine("POST /api/v1/system/register")
    RegisterVisitorOutput registerVisitor(@RequestBody RegisterVisitorInput input);


    @RequestLine("GET /api/v1/system/register?startDate={startDate}&endDate={endDate}&firstName={firstName}&" +
            "lastName={lastName}&phoneNumber={phoneNumber}&idCardNumber={idCardNumber}&idCardValidity={idCardValidity}&" +
            "idCardIssueAuthority={idCardIssueAuthority}&idCardIssueDate={idCardIssueDate}&roomNumber={roomNumber}")
    GetVisitorsOutput getVisitors(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("phoneNumber") String phoneNumber,
            @Param("idCardNumber") String idCardNumber,
            @Param("idCardValidity") LocalDate idCardValidity,
            @Param("idCardIssueAuthority") String idCardIssueAuthority,
            @Param("idCardIssueDate") LocalDate idCardIssueDate,
            @Param("roomNumber") String roomNumber
    );

    @RequestLine("POST /api/v1/system/room")
    AddRoomOutput addRoom(@RequestBody AddRoomInput input);

    @RequestLine("PUT /api/v1/system/room/{roomId}")
    UpdateRoomOutput updateRoom(@Param("roomId") String roomId, @RequestBody UpdateRoomInput input);

    @RequestLine("PATCH /api/v1/system/room/{roomId}")
    PartialUpdateRoomOutput partialUpdateRoom(@Param("roomId") String roomId, @RequestBody PartialUpdateRoomInput input);

    @RequestLine("DELETE /api/v1/system/room/{roomId}")
    DeleteRoomOutput deleteRoom(@Param("roomId") String roomId);

}
