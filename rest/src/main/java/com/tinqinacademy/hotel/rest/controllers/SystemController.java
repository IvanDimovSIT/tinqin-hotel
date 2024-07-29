package com.tinqinacademy.hotel.rest.controllers;


import com.tinqinacademy.hotel.api.operations.system.addroom.AddRoomInput;
import com.tinqinacademy.hotel.api.operations.system.addroom.AddRoomOutput;
import com.tinqinacademy.hotel.api.operations.system.addroom.AddRoomService;
import com.tinqinacademy.hotel.api.operations.system.deleteroom.DeleteRoomInput;
import com.tinqinacademy.hotel.api.operations.system.deleteroom.DeleteRoomOutput;
import com.tinqinacademy.hotel.api.operations.system.deleteroom.DeleteRoomService;
import com.tinqinacademy.hotel.api.operations.system.getvisitors.GetVisitorsInput;
import com.tinqinacademy.hotel.api.operations.system.getvisitors.GetVisitorsOutput;
import com.tinqinacademy.hotel.api.operations.system.getvisitors.GetVisitorsService;
import com.tinqinacademy.hotel.api.operations.system.partialupdateroom.PartialUpdateRoomInput;
import com.tinqinacademy.hotel.api.operations.system.partialupdateroom.PartialUpdateRoomOutput;
import com.tinqinacademy.hotel.api.operations.system.partialupdateroom.PartialUpdateRoomService;
import com.tinqinacademy.hotel.api.operations.system.registervisitor.RegisterVisitorInput;
import com.tinqinacademy.hotel.api.operations.system.registervisitor.RegisterVisitorOutput;
import com.tinqinacademy.hotel.api.operations.system.registervisitor.RegisterVisitorService;
import com.tinqinacademy.hotel.api.operations.system.updateroom.UpdateRoomInput;
import com.tinqinacademy.hotel.api.operations.system.updateroom.UpdateRoomOutput;
import com.tinqinacademy.hotel.api.operations.system.updateroom.UpdateRoomService;
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
public class SystemController {
    private final RegisterVisitorService registerVisitorService;
    private final GetVisitorsService getVisitorsService;
    private final AddRoomService addRoomService;
    private final UpdateRoomService updateRoomService;
    private final PartialUpdateRoomService partialUpdateRoomService;
    private final DeleteRoomService deleteRoomService;

    @Operation(summary = "Registers a visitor as room renter", description = "Registers a visitor as room renter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Success"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Room id not found")
    })
    @PostMapping(RestApiRoutes.SYSTEM_REGISTER_VISITOR)
    public ResponseEntity<?> registerVisitor(@Valid @RequestBody RegisterVisitorInput input) {

        RegisterVisitorOutput output = registerVisitorService.process(input);

        return new ResponseEntity<>(
                output,
                HttpStatus.OK
        );
    }

    @Operation(summary = "Finds info on visitors", description = "Admin only. Provides a report based on" +
            " various criteria. Provides info on when the room was booked and by whom. Can report when a user has" +
            " occupied rooms.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping(RestApiRoutes.SYSTEM_GET_VISITORS)
    public ResponseEntity<?> getVisitors(
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
    ) {
        GetVisitorsInput input = GetVisitorsInput.builder()
                .startDate(startDate)
                .endDate(endDate)
                .firstName(firstName)
                .lastName(lastName)
                .phoneNumber(phoneNumber)
                .idCardIssueAuthority(idCardIssueAuthority)
                .idCardNumber(idCardNumber)
                .idCardValidity(idCardValidity)
                .idCardIssueDate(idCardIssueDate)
                .roomNumber(roomNumber)
                .build();

        GetVisitorsOutput output = getVisitorsService.process(input);

        return new ResponseEntity<>(
                output,
                HttpStatus.OK
        );
    }


    @Operation(summary = "Creates a new room", description = "Admin creates a new room with specified " +
            "parameters.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully added room"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PostMapping(RestApiRoutes.SYSTEM_ADD_ROOM)
    public ResponseEntity<?> addRoom(@Valid @RequestBody AddRoomInput input) {
        AddRoomOutput output = addRoomService.process(input);

        return new ResponseEntity<>(
                output,
                HttpStatus.CREATED
        );
    }

    @Operation(summary = "Update room data", description = "Admin updates the info regarding a certain" +
            " room")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success updated room data"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Room id not found"),
    })
    @PutMapping(RestApiRoutes.SYSTEM_UPDATE_ROOM)
    public ResponseEntity<?> updateRoom(@PathVariable String roomId, @Valid @RequestBody UpdateRoomInput input) {
        UpdateRoomInput updateRoomInput = input.toBuilder()
                .roomId(roomId)
                .build();

        UpdateRoomOutput output = updateRoomService.process(updateRoomInput);

        return new ResponseEntity<>(
                output,
                HttpStatus.OK
        );
    }

    @Operation(summary = "Admin partial update of room data", description = "Admin partial update of room data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Room data updated"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Room id not found"),
    })
    @PatchMapping(value = RestApiRoutes.SYSTEM_PARTIAL_UPDATE_ROOM, consumes = {"application/json-patch+json", "application/json"})
    public ResponseEntity<?> partialUpdateRoom(@PathVariable String roomId, @Valid @RequestBody PartialUpdateRoomInput input) {
        PartialUpdateRoomInput partialUpdateRoomInput = input.toBuilder()
                .roomId(roomId)
                .build();

        PartialUpdateRoomOutput output = partialUpdateRoomService.process(partialUpdateRoomInput);

        return new ResponseEntity<>(
                output,
                HttpStatus.OK
        );
    }

    @Operation(summary = "Deletes a room", description = "Deletes a room" +
            " is available")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Room deleted"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "room id not found"),
    })
    @DeleteMapping(RestApiRoutes.SYSTEM_DELETE_ROOM)
    public ResponseEntity<?> deleteRoom(@PathVariable String roomId) {
        DeleteRoomInput input = DeleteRoomInput.builder()
                .id(roomId)
                .build();

        DeleteRoomOutput output = deleteRoomService.process(input);

        return new ResponseEntity<>(
                output,
                HttpStatus.OK
        );
    }
}
