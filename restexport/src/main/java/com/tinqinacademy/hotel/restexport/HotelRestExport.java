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


//@FeignClient(name = "hotel")
@Headers({"Content-Type: application/json"})
public interface HotelRestExport {

    @RequestLine("GET /api/v1/hotel/rooms?startDate={startDate}&endDate={endDate}&bedCount={bedCount}&bedSize={bedSize}&bathroomType={bathroomType}")
    //@GetMapping(RestApiRoutes.HOTEL_GET_AVAILABLE_ROOMS)
    /*ResponseEntity<?>*/CheckAvailableRoomsOutput checkAvailableRooms(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("bedCount") Integer bedCount,
            @Param("bedSize") String bedSize,
            @Param("bathroomType") String bathroomType);

    @RequestLine("GET /api/v1/hotel/{roomId}")
    //@GetMapping(RestApiRoutes.HOTEL_GET_ROOM)
    /*ResponseEntity<?>*/GetRoomOutput getRoom(@Param("roomId") String roomId);

    @RequestLine("POST /api/v1/hotel/{roomId}")
    //@PostMapping(RestApiRoutes.HOTEL_BOOK_ROOM)
    /*ResponseEntity<?>*/BookRoomOutput bookRoom(@Param("roomId") String roomId, @RequestBody BookRoomInput bookRoomInput);


    @RequestLine("DELETE /api/v1/hotel/{bookingId}")
    //@DeleteMapping(RestApiRoutes.HOTEL_UNBOOK_ROOM)
    /*ResponseEntity<?>*/UnbookRoomOutput unbookRoom(@Param("bookingId") String bookingId, @RequestBody UnbookRoomInput unbookRoomInput);


    @RequestLine("POST /api/v1/system/register")
    //@PostMapping(RestApiRoutes.SYSTEM_REGISTER_VISITOR)
    /*ResponseEntity<?>*/RegisterVisitorOutput registerVisitor(@RequestBody RegisterVisitorInput input);


    @RequestLine("GET /api/v1/system/register?startDate={startDate}&endDate={endDate}&firstName={firstName}&" +
            "lastName={lastName}&phoneNumber={phoneNumber}&idCardNumber={idCardNumber}&idCardValidity={idCardValidity}&" +
            "idCardIssueAuthority={idCardIssueAuthority}&idCardIssueDate={idCardIssueDate}&roomNumber={roomNumber}")
    //@GetMapping(RestApiRoutes.SYSTEM_GET_VISITORS)
    /*ResponseEntity<?>*/GetVisitorsOutput getVisitors(
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
    //@PostMapping(RestApiRoutes.SYSTEM_ADD_ROOM)
    /*ResponseEntity<?>*/AddRoomOutput addRoom(@RequestBody AddRoomInput input);

    @RequestLine("PUT /api/v1/system/room/{roomId}")
    //@PutMapping(RestApiRoutes.SYSTEM_UPDATE_ROOM)
    /*ResponseEntity<?>*/UpdateRoomOutput updateRoom(@Param("roomId") String roomId, @RequestBody UpdateRoomInput input);

    //@PatchMapping(value = RestApiRoutes.SYSTEM_PARTIAL_UPDATE_ROOM, consumes = {"application/json-patch+json", "application/json"})
    @RequestLine("PATCH /api/v1/system/room/{roomId}")
    /*ResponseEntity<?>*/PartialUpdateRoomOutput partialUpdateRoom(@Param("roomId") String roomId, @RequestBody PartialUpdateRoomInput input);


    @RequestLine("DELETE /api/v1/system/room/{roomId}")
    //@DeleteMapping(RestApiRoutes.SYSTEM_DELETE_ROOM)
    /*ResponseEntity<?>*/DeleteRoomOutput deleteRoom(@Param("roomId") String roomId);

}
