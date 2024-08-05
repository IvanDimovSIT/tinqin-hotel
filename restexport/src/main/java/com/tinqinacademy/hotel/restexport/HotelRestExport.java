package com.tinqinacademy.hotel.restexport;


import com.tinqinacademy.hotel.api.RestApiRoutes;
import com.tinqinacademy.hotel.api.operations.hotel.bookroom.BookRoomInput;
import com.tinqinacademy.hotel.api.operations.system.addroom.AddRoomInput;
import com.tinqinacademy.hotel.api.operations.system.partialupdateroom.PartialUpdateRoomInput;
import com.tinqinacademy.hotel.api.operations.system.registervisitor.RegisterVisitorInput;
import com.tinqinacademy.hotel.api.operations.system.updateroom.UpdateRoomInput;
import feign.Headers;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


@FeignClient(name = "hotel")
@Headers({"Content-Type: application/json"})
public interface HotelRestExport {

    //@RequestLine("GET "+RestApiRoutes.HOTEL_GET_AVAILABLE_ROOMS)
    @GetMapping(RestApiRoutes.HOTEL_GET_AVAILABLE_ROOMS)
    ResponseEntity<?> checkAvailableRooms(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam(required = false) Integer bedCount,
            @RequestParam(required = false) String bedSize,
            @RequestParam(required = false) String bathroomType);

    //@RequestLine("GET "+RestApiRoutes.HOTEL_GET_ROOM)
    @GetMapping(RestApiRoutes.HOTEL_GET_ROOM)
    ResponseEntity<?> getRoom(@PathVariable String roomId);

    //@RequestLine("POST "+RestApiRoutes.HOTEL_BOOK_ROOM)
    @PostMapping(RestApiRoutes.HOTEL_BOOK_ROOM)
    ResponseEntity<?> bookRoom(@PathVariable String roomId, @RequestBody BookRoomInput bookRoomInput);


    //@RequestLine("DELETE "+RestApiRoutes.HOTEL_UNBOOK_ROOM)
    @DeleteMapping(RestApiRoutes.HOTEL_UNBOOK_ROOM)
    ResponseEntity<?> unbookRoom(@PathVariable String bookingId);


    //@RequestLine("POST "+RestApiRoutes.SYSTEM_REGISTER_VISITOR)
    @PostMapping(RestApiRoutes.SYSTEM_REGISTER_VISITOR)
    ResponseEntity<?> registerVisitor(@RequestBody RegisterVisitorInput input);


    //@RequestLine("GET "+RestApiRoutes.SYSTEM_GET_VISITORS)
    @GetMapping(RestApiRoutes.SYSTEM_GET_VISITORS)
    ResponseEntity<?> getVisitors(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String idCardNumber,
            @RequestParam(required = false) LocalDate idCardValidity,
            @RequestParam(required = false) String idCardIssueAuthority,
            @RequestParam(required = false) LocalDate idCardIssueDate,
            @RequestParam(required = false) String roomNumber
    );

    //@RequestLine("POST "+RestApiRoutes.SYSTEM_ADD_ROOM)
    @PostMapping(RestApiRoutes.SYSTEM_ADD_ROOM)
    ResponseEntity<?> addRoom(@RequestBody AddRoomInput input);

    //@RequestLine("PUT "+RestApiRoutes.SYSTEM_UPDATE_ROOM)
    @PutMapping(RestApiRoutes.SYSTEM_UPDATE_ROOM)
    ResponseEntity<?> updateRoom(@PathVariable String roomId, @RequestBody UpdateRoomInput input);

    @PatchMapping(value = RestApiRoutes.SYSTEM_PARTIAL_UPDATE_ROOM, consumes = {"application/json-patch+json", "application/json"})
    //@RequestLine("PATCH "+RestApiRoutes.SYSTEM_UPDATE_ROOM)
    ResponseEntity<?> partialUpdateRoom(@PathVariable String roomId, @RequestBody PartialUpdateRoomInput input);


    //@RequestLine("DELETE "+RestApiRoutes.SYSTEM_DELETE_ROOM)
    @DeleteMapping(RestApiRoutes.SYSTEM_DELETE_ROOM)
    ResponseEntity<?> deleteRoom(@PathVariable String roomId);

}
