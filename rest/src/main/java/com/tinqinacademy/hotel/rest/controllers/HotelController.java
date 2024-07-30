package com.tinqinacademy.hotel.rest.controllers;

import com.tinqinacademy.hotel.api.model.enums.BathroomType;
import com.tinqinacademy.hotel.api.model.enums.BedSize;
import com.tinqinacademy.hotel.api.operations.hotel.bookroom.BookRoomInput;
import com.tinqinacademy.hotel.api.operations.hotel.bookroom.BookRoomOutput;
import com.tinqinacademy.hotel.api.operations.hotel.bookroom.BookRoomOperation;
import com.tinqinacademy.hotel.api.operations.hotel.checkavailablerooms.CheckAvailableRoomsInput;
import com.tinqinacademy.hotel.api.operations.hotel.checkavailablerooms.CheckAvailableRoomsOutput;
import com.tinqinacademy.hotel.api.operations.hotel.checkavailablerooms.CheckAvailableRoomsOperation;
import com.tinqinacademy.hotel.api.operations.hotel.getroom.GetRoomInput;
import com.tinqinacademy.hotel.api.operations.hotel.getroom.GetRoomOutput;
import com.tinqinacademy.hotel.api.operations.hotel.getroom.GetRoomOperation;
import com.tinqinacademy.hotel.api.operations.hotel.unbookroom.UnbookRoomInput;
import com.tinqinacademy.hotel.api.operations.hotel.unbookroom.UnbookRoomOutput;
import com.tinqinacademy.hotel.api.operations.hotel.unbookroom.UnbookRoomOperation;
import com.tinqinacademy.hotel.api.RestApiRoutes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class HotelController {
    private final CheckAvailableRoomsOperation checkAvailableRoomsService;
    private final GetRoomOperation getRoomOperation;
    private final BookRoomOperation bookRoomService;
    private final UnbookRoomOperation unbookRoomOperation;

    @Operation(summary = "Checks whether a room is available for a certain period", description = "Checks whether a" +
            " room is available for a certain period. Room requirements should come as query parameters in URL.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping(RestApiRoutes.HOTEL_GET_AVAILABLE_ROOMS)
    public ResponseEntity<?> checkAvailableRooms(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam(required = false) Integer bedCount,
            @RequestParam(required = false) String bedSize,
            @RequestParam(required = false) String bathroomType) {
        CheckAvailableRoomsInput input = CheckAvailableRoomsInput.builder()
                .startDate(startDate)
                .endDate(endDate)
                .bedCount(bedCount)
                .bedSize(bedSize ==null? null: BedSize.getCode(bedSize))
                .bathroomType(bathroomType == null? null: BathroomType.getCode(bathroomType))
                .build();


        CheckAvailableRoomsOutput output = checkAvailableRoomsService.process(input);

        return new ResponseEntity<>(
                output,
                HttpStatus.OK
        );
    }


    @Operation(summary = "Returns basic info for a room", description = "Returns basic info for a room with " +
            "specified id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Room found with booking information"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Room id not found"),
    })
    @GetMapping(RestApiRoutes.HOTEL_GET_ROOM)
    public ResponseEntity<?> getRoom(@PathVariable String roomId) {
        GetRoomInput input = GetRoomInput.builder()
                .id(roomId)
                .build();

        GetRoomOutput output = getRoomOperation.process(input);

        return new ResponseEntity<>(
                output,
                HttpStatus.OK
        );
    }

    @Operation(summary = "Books the room", description = "Books the room specified")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Room booked"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Room id not found"),
    })
    @PostMapping(RestApiRoutes.HOTEL_BOOK_ROOM)
    public ResponseEntity<?> bookRoom(@PathVariable String roomId, @Valid @RequestBody BookRoomInput bookRoomInput) {
        BookRoomInput input = bookRoomInput.toBuilder()
                .id(roomId)
                .build();

        BookRoomOutput output = bookRoomService.process(input);

        return new ResponseEntity<>(
                output,
                HttpStatus.CREATED
        );
    }

    @Operation(summary = "Unbooks a booked room", description = "Unbooks a room that the user has already" +
            " booked")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Booking id not found"),
    })
    @DeleteMapping(RestApiRoutes.HOTEL_UNBOOK_ROOM)
    public ResponseEntity<?> unbookRoom(@PathVariable String bookingId) {
        UnbookRoomInput input = UnbookRoomInput.builder()
                .bookingId(bookingId)
                .build();

        UnbookRoomOutput output = unbookRoomOperation.process(input);

        return new ResponseEntity<>(
                output,
                HttpStatus.OK
        );
    }




}
