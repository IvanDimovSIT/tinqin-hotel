package com.tinqinacademy.hotel.core.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.tinqinacademy.hotel.api.model.visitor.VisitorOutput;
import com.tinqinacademy.hotel.api.operations.system.addroom.AddRoomInput;
import com.tinqinacademy.hotel.api.operations.system.addroom.AddRoomOutput;
import com.tinqinacademy.hotel.api.operations.system.deleteroom.DeleteRoomInput;
import com.tinqinacademy.hotel.api.operations.system.deleteroom.DeleteRoomOutput;
import com.tinqinacademy.hotel.api.operations.system.getvisitors.GetVisitorsInput;
import com.tinqinacademy.hotel.api.operations.system.getvisitors.GetVisitorsOutput;
import com.tinqinacademy.hotel.api.operations.system.partialupdateroom.PartialUpdateRoomInput;
import com.tinqinacademy.hotel.api.operations.system.partialupdateroom.PartialUpdateRoomOutput;
import com.tinqinacademy.hotel.api.operations.system.registervisitor.RegisterVisitorInput;
import com.tinqinacademy.hotel.api.operations.system.registervisitor.RegisterVisitorOutput;
import com.tinqinacademy.hotel.api.operations.system.updateroom.UpdateRoomInput;
import com.tinqinacademy.hotel.api.operations.system.updateroom.UpdateRoomOutput;
import com.tinqinacademy.hotel.api.services.SystemService;
import com.tinqinacademy.hotel.core.exception.exceptions.CreateRoomException;
import com.tinqinacademy.hotel.core.exception.exceptions.DeleteRoomException;
import com.tinqinacademy.hotel.core.exception.exceptions.NotFoundException;
import com.tinqinacademy.hotel.core.exception.exceptions.RegisterVisitorException;
import com.tinqinacademy.hotel.persistence.model.Bed;
import com.tinqinacademy.hotel.persistence.model.Booking;
import com.tinqinacademy.hotel.persistence.model.Guest;
import com.tinqinacademy.hotel.persistence.model.Room;
import com.tinqinacademy.hotel.persistence.model.enums.BathroomType;
import com.tinqinacademy.hotel.persistence.model.enums.BedSize;
import com.tinqinacademy.hotel.persistence.repository.BedRepository;
import com.tinqinacademy.hotel.persistence.repository.BookingRepository;
import com.tinqinacademy.hotel.persistence.repository.GuestRepository;
import com.tinqinacademy.hotel.persistence.repository.RoomRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
@Slf4j
public class SystemServiceImpl implements SystemService {
    private final GuestRepository guestRepository;
    private final RoomRepository roomRepository;
    private final BedRepository bedRepository;
    private final BookingRepository bookingRepository;
    private final ObjectMapper objectMapper;
    private final ConversionService conversionService;

    @Override
    public RegisterVisitorOutput registerVisitor(RegisterVisitorInput input) {
        log.info("Start registerVisitor input:{}", input);


        List<Guest> guests = input.getVisitorInputs().stream()
                .map(visitor -> {
                    if (!(visitor.getIdCardNumber() != null && visitor.getIdCardIssueDate() != null &&
                            visitor.getIdCardIssueAuthority() != null && visitor.getIdCardValidity() != null) &&
                            !(visitor.getIdCardNumber() == null && visitor.getIdCardIssueDate() == null &&
                                    visitor.getIdCardIssueAuthority() == null && visitor.getIdCardValidity() == null)) {
                        throw new RegisterVisitorException("Error registering visitor: invalid id information");
                    }

                    Guest guest = conversionService.convert(visitor, Guest.class);
                    guest = guestRepository.save(guest);
                    Booking booking = bookingRepository.findByRoomIdAndStartDateAndEndDate(
                                    UUID.fromString(visitor.getRoomId()),
                                    visitor.getStartDate(),
                                    visitor.getEndDate())
                            .orElseThrow(
                                    () -> new NotFoundException(
                                            "Booking not found with roomId:" + visitor.getRoomId() +
                                                    " start date:" + visitor.getStartDate() +
                                                    " end date:" + visitor.getEndDate()));

                    booking.getGuests().add(guest);
                    bookingRepository.save(booking);

                    return guest;
                })
                .toList();


        log.info("registerVisitor created guests:{}", guests);

        RegisterVisitorOutput result = RegisterVisitorOutput.builder()
                .build();

        log.info("End registerVisitor result:{}", result);

        return result;
    }

    @Override
    public GetVisitorsOutput getVisitors(GetVisitorsInput input) {
        log.info("Start getVisitors input:{}", input);

        List<Booking> bookings = bookingRepository.findBookingsByCriteria(
                input.getStartDate(),
                input.getEndDate(),
                input.getFirstName(),
                input.getLastName(),
                input.getPhoneNumber(),
                input.getIdCardNumber(),
                input.getIdCardValidity(),
                input.getIdCardIssueAuthority(),
                input.getIdCardIssueDate(),
                input.getRoomNumber());

        List<VisitorOutput> visitorOutputs = new ArrayList<>();
        for (Booking booking : bookings) {
            for (Guest guest : booking.getGuests()) {
                if (input.getFirstName() != null && !input.getFirstName().equals(guest.getFirstName())) {
                    continue;
                }
                if (input.getLastName() != null && !input.getLastName().equals(guest.getLastName())) {
                    continue;
                }
                if (input.getIdCardNumber() != null && !input.getIdCardNumber().equals(guest.getIdCardNo())) {
                    continue;
                }
                if (input.getIdCardValidity() != null && !input.getIdCardValidity().equals(guest.getIdCardValidity())) {
                    continue;
                }
                if (input.getIdCardIssueAuthority() != null && !input.getIdCardIssueAuthority().equals(guest.getIdCardIssueAuthority())) {
                    continue;
                }
                if (input.getIdCardIssueDate() != null && !input.getIdCardIssueDate().equals(guest.getIdCardIssueDate())) {
                    continue;
                }
                VisitorOutput visitorOutput = conversionService.convert(input, VisitorOutput.class);
                visitorOutput.setPhoneNumber(booking.getUser().getPhoneNumber());
                visitorOutput.setRoomId(booking.getRoom().getId().toString());
                visitorOutput.setStartDate(booking.getStartDate());
                visitorOutput.setEndDate(booking.getEndDate());

                visitorOutputs.add(visitorOutput);
            }
        }

        GetVisitorsOutput result = GetVisitorsOutput.builder()
                .visitorOutputs(visitorOutputs)
                .build();

        log.info("End getVisitors result:{}", result);

        return result;
    }

    @Override
    public AddRoomOutput addRoom(AddRoomInput input) {
        log.info("Start addRoom input:{}", input);

        Optional<Bed> bed = bedRepository.findByBedSize(BedSize.getCode(input.getBedSize().toString()));
        if (bed.isEmpty()) {
            throw new NotFoundException("Beds with type " + input.getBedSize().toString() + " not found");
        }
        if (roomRepository.findByRoomNo(input.getRoomNumber()).isPresent()) {
            throw new CreateRoomException("Room with number " + input.getRoomNumber() + " already exists");
        }

        List<Bed> bedsToAdd = IntStream.range(0, input.getBedCount()).mapToObj(i -> bed.get())
                .collect(Collectors.toList());

        Room room = conversionService.convert(input, Room.class);
        room.setBeds(bedsToAdd);

        room = roomRepository.save(room);

        AddRoomOutput result = conversionService.convert(room, AddRoomOutput.class);

        log.info("End addRoom result:{}", result);

        return result;
    }

    @Override
    public UpdateRoomOutput updateRoom(UpdateRoomInput input) {
        log.info("Start updateRoom input:{}", input);

        Room room = roomRepository.findById(UUID.fromString(input.getRoomId())).orElseThrow(
                () -> new NotFoundException("Room with id:" + input.getRoomId() + " not found"));

        room.setRoomNo(input.getRoomNumber());
        room.setPrice(input.getPrice());
        room.setFloor(input.getFloor());
        room.setBathroomType(BathroomType.getCode(input.getBathroomType().toString()));

        List<Bed> beds;
        Bed bedToAdd = bedRepository.findByBedSize(BedSize.getCode(input.getBedSize().toString())).get();
        beds = IntStream.range(0, input.getBedCount()).mapToObj(i -> bedToAdd).collect(Collectors.toList());
        room.setBeds(beds);

        room = roomRepository.save(room);

        UpdateRoomOutput result = conversionService.convert(room, UpdateRoomOutput.class);

        log.info("End updateRoom result:{}", result);

        return result;
    }

    @Override
    public PartialUpdateRoomOutput partialUpdateRoom(PartialUpdateRoomInput input) {
        log.info("Start partialUpdateRoom input:{}", input);

        Room currentRoom = roomRepository.findById(UUID.fromString(input.getRoomId())).orElseThrow(
                () -> new NotFoundException("Room with id:" + input.getRoomId() + " not found"));

        List<Bed> newBeds = new ArrayList<>();
        Bed bedToAdd = currentRoom.getBeds().getFirst();
        int numberOfbedsToAdd = currentRoom.getBeds().size();
        if (input.getBedSize() != null) {
            bedToAdd = bedRepository.findByBedSize(BedSize.getCode(input.getBedSize().toString())).get();
        }
        if (input.getBedCount() != null) {
            numberOfbedsToAdd = input.getBedCount();
        }
        for (int i = 0; i < numberOfbedsToAdd; i++) {
            newBeds.add(bedToAdd);
        }

        Room newRoom = conversionService.convert(input, Room.class);
        newRoom.setBeds(newBeds);


        JsonNode roomNode = objectMapper.valueToTree(currentRoom);
        JsonNode inputNode = objectMapper.valueToTree(newRoom);

        try {
            JsonMergePatch patch = JsonMergePatch.fromJson(inputNode);
            newRoom = objectMapper.treeToValue(patch.apply(roomNode), Room.class);
        } catch (JsonPatchException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        newRoom = roomRepository.save(newRoom);

        PartialUpdateRoomOutput result = conversionService.convert(newRoom, PartialUpdateRoomOutput.class);

        log.info("End partialUpdateRoom result:{}", result);

        return result;
    }

    @Override
    public DeleteRoomOutput deleteRoom(DeleteRoomInput input) {
        log.info("Start deleteRoom input:{}", input);

        Room room = roomRepository.findById(UUID.fromString(input.getId())).orElseThrow(
                () -> new NotFoundException("Room with id:" + input.getId() + " not found"));

        LocalDate today = LocalDate.now();
        boolean isRoomOccupied = bookingRepository.findAllByRoomId(room.getId()).stream()
                .anyMatch(booking -> !today.isAfter(booking.getEndDate()));

        if (isRoomOccupied) {
            throw new DeleteRoomException("Room with id:" + room.getId() + " is occupied");
        }

        roomRepository.delete(room);

        DeleteRoomOutput result = DeleteRoomOutput.builder()
                .build();

        log.info("End deleteRoom result:{}", result);

        return result;
    }
}
