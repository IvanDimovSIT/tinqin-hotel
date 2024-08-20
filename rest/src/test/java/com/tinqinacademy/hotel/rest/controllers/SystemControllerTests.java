package com.tinqinacademy.hotel.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinqinacademy.hotel.api.RestApiRoutes;
import com.tinqinacademy.hotel.api.model.visitor.VisitorInput;
import com.tinqinacademy.hotel.api.operations.system.addroom.AddRoomInput;
import com.tinqinacademy.hotel.api.operations.system.partialupdateroom.PartialUpdateRoomInput;
import com.tinqinacademy.hotel.api.operations.system.registervisitor.RegisterVisitorInput;
import com.tinqinacademy.hotel.api.operations.system.updateroom.UpdateRoomInput;
import com.tinqinacademy.hotel.persistence.model.Bed;
import com.tinqinacademy.hotel.persistence.model.Booking;
import com.tinqinacademy.hotel.persistence.model.Room;
import com.tinqinacademy.hotel.persistence.model.enums.BathroomType;
import com.tinqinacademy.hotel.persistence.model.enums.BedSize;
import com.tinqinacademy.hotel.persistence.repository.BedRepository;
import com.tinqinacademy.hotel.persistence.repository.BookingRepository;
import com.tinqinacademy.hotel.persistence.repository.RoomRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY, connection = EmbeddedDatabaseConnection.H2)
public class SystemControllerTests {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private BedRepository bedRepository;
    @Autowired
    private BookingRepository bookingRepository;

    @BeforeEach
    public void setup() {

        Bed bed = bedRepository.findByBedSize(BedSize.SINGLE).get();

        Room room = Room.builder()
                .roomNo("123")
                .bathroomType(BathroomType.SHARED)
                .beds(List.of(bed))
                .price(BigDecimal.valueOf(678.32))
                .floor(3)
                .build();

        room = roomRepository.save(room);

        Booking booking = Booking.builder()
                .startDate(LocalDate.of(2029, 9, 27))
                .endDate(LocalDate.of(2029, 9, 29))
                .room(room)
                .userId(UUID.randomUUID())
                .totalPrice(BigDecimal.valueOf(1000))
                .guests(new HashSet<>())
                .build();

        bookingRepository.save(booking);

    }

    @AfterEach
    public void clearDB() {
        roomRepository.deleteAll();
        bookingRepository.deleteAll();
    }

    @Test
    public void testRegisterVisitorOk() throws Exception {
        Booking booking = bookingRepository.findAll().getFirst();

        VisitorInput visitorInput = VisitorInput.builder()
                .firstName("Kolyo")
                .lastName("Zafirov")
                .idCardNumber("123354596")
                .idCardIssueAuthority("ABCDEF")
                .idCardValidity(LocalDate.now())
                .idCardIssueDate(LocalDate.now())
                .phoneNumber(null)
                .dateOfBirth(LocalDate.of(2000, 10, 4))
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .roomId(booking.getRoom().getId().toString())
                .build();

        RegisterVisitorInput input = RegisterVisitorInput.builder()
                .visitorInputs(List.of(visitorInput))
                .build();

        mvc.perform(post(RestApiRoutes.SYSTEM_REGISTER_VISITOR)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(input)))
                .andExpect(status().isOk());
    }

    @Test
    public void testRegisterVisitorBadRequest() throws Exception {
        Room room = roomRepository.findAll().getFirst();

        VisitorInput visitorInput = VisitorInput.builder()
                .firstName("Kolyo")
                .lastName("Zafirov")
                .idCardNumber("1233545")
                .idCardIssueAuthority("ABCDEF")
                .idCardValidity(LocalDate.now())
                .idCardIssueDate(LocalDate.now())
                .phoneNumber(null)
                .dateOfBirth(LocalDate.of(2000, 10, 4))
                .startDate(LocalDate.of(2030, 10, 12))
                .endDate(LocalDate.of(2030, 10, 12))
                .roomId(room.getId().toString())
                .build();

        RegisterVisitorInput input = RegisterVisitorInput.builder()
                .visitorInputs(List.of(visitorInput))
                .build();

        mvc.perform(post(RestApiRoutes.SYSTEM_REGISTER_VISITOR)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetVisitorsOk() throws Exception {
        mvc.perform(get(RestApiRoutes.SYSTEM_GET_VISITORS)
                        .param("startDate", "2024-01-01")
                        .param("endDate", "2024-12-31")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetVisitorsBadRequest() throws Exception {
        mvc.perform(get(RestApiRoutes.SYSTEM_GET_VISITORS)
                        .param("startDate", "abc")
                        .param("endDate", "2024-12-31")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddRoomCreated() throws Exception {
        AddRoomInput input = AddRoomInput.builder()
                .bedCount(2)
                .price(BigDecimal.valueOf(600))
                .floor(5)
                .bedSize(com.tinqinacademy.hotel.api.model.enums.BedSize.SINGLE)
                .bathroomType(com.tinqinacademy.hotel.api.model.enums.BathroomType.PRIVATE)
                .roomNumber("101")
                .build();

        mvc.perform(post(RestApiRoutes.SYSTEM_ADD_ROOM)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(input)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testAddRoomBadRequest() throws Exception {
        AddRoomInput input = AddRoomInput.builder()
                .roomNumber("123")
                .build();

        mvc.perform(post(RestApiRoutes.SYSTEM_ADD_ROOM)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateRoomOk() throws Exception {
        Room room = roomRepository.findAll().getFirst();

        UpdateRoomInput input = UpdateRoomInput.builder()
                .roomId(room.getId().toString())
                .bedCount(2)
                .price(BigDecimal.valueOf(600))
                .floor(5)
                .bedSize(com.tinqinacademy.hotel.api.model.enums.BedSize.SINGLE)
                .bathroomType(com.tinqinacademy.hotel.api.model.enums.BathroomType.PRIVATE)
                .roomNumber("101")
                .build();

        mvc.perform(put(RestApiRoutes.SYSTEM_UPDATE_ROOM, input.getRoomId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(input)))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateRoomNotFound() throws Exception {
        UpdateRoomInput input = UpdateRoomInput.builder()
                .roomId(UUID.randomUUID().toString())
                .bedCount(2)
                .price(BigDecimal.valueOf(600))
                .floor(5)
                .bedSize(com.tinqinacademy.hotel.api.model.enums.BedSize.SINGLE)
                .bathroomType(com.tinqinacademy.hotel.api.model.enums.BathroomType.PRIVATE)
                .roomNumber("101")
                .build();

        mvc.perform(put(RestApiRoutes.SYSTEM_UPDATE_ROOM, UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(input)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateRoomBadRequestInvalidRequestBody() throws Exception {
        Room room = roomRepository.findAll().getFirst();

        UpdateRoomInput input = UpdateRoomInput.builder()
                .roomId(UUID.randomUUID().toString())
                .bedCount(2)
                .price(BigDecimal.valueOf(600))
                .floor(-1329)
                .bedSize(com.tinqinacademy.hotel.api.model.enums.BedSize.SINGLE)
                .bathroomType(com.tinqinacademy.hotel.api.model.enums.BathroomType.PRIVATE)
                .roomNumber("101")
                .build();

        mvc.perform(put(RestApiRoutes.SYSTEM_UPDATE_ROOM, room.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testUpdateRoomBadRequestInvalidId() throws Exception {
        UpdateRoomInput input = UpdateRoomInput.builder()
                .roomId(UUID.randomUUID().toString())
                .bedCount(2)
                .price(BigDecimal.valueOf(600))
                .floor(4)
                .bedSize(com.tinqinacademy.hotel.api.model.enums.BedSize.SINGLE)
                .bathroomType(com.tinqinacademy.hotel.api.model.enums.BathroomType.PRIVATE)
                .roomNumber("101")
                .build();

        mvc.perform(put(RestApiRoutes.SYSTEM_UPDATE_ROOM, "1236")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testPartialUpdateRoomOk() throws Exception {
        PartialUpdateRoomInput input = PartialUpdateRoomInput.builder()
                .price(BigDecimal.valueOf(1235))
                .build();

        Room roomToUpdate = Room.builder()
                .roomNo("344")
                .bathroomType(BathroomType.SHARED)
                .beds(new ArrayList<>(List.of(bedRepository.findByBedSize(BedSize.SINGLE).get())))
                .price(BigDecimal.valueOf(100.77))
                .floor(3)
                .build();

        roomToUpdate = roomRepository.save(roomToUpdate);

        mvc.perform(patch(RestApiRoutes.SYSTEM_PARTIAL_UPDATE_ROOM, roomToUpdate.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(input)))
                .andExpect(status().isOk());
    }

    @Test
    public void testPartialUpdateRoomBadRequest() throws Exception {
        PartialUpdateRoomInput input = PartialUpdateRoomInput.builder()
                .price(BigDecimal.valueOf(1235))
                .build();

        Room roomToUpdate = Room.builder()
                .roomNo("344")
                .bathroomType(BathroomType.SHARED)
                .beds(new ArrayList<>(List.of(bedRepository.findByBedSize(BedSize.SINGLE).get())))
                .price(BigDecimal.valueOf(100.77))
                .floor(3)
                .build();

        roomRepository.save(roomToUpdate);

        mvc.perform(patch(RestApiRoutes.SYSTEM_PARTIAL_UPDATE_ROOM, "123423")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testPartialUpdateRoomNotFound() throws Exception {
        PartialUpdateRoomInput input = PartialUpdateRoomInput.builder()
                .roomId(UUID.randomUUID().toString())
                .floor(3)
                .build();

        mvc.perform(patch(RestApiRoutes.SYSTEM_PARTIAL_UPDATE_ROOM, UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(input)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteRoomOk() throws Exception {
        Room roomToDelete = Room.builder()
                .roomNo("456")
                .bathroomType(BathroomType.SHARED)
                .beds(List.of(bedRepository.findByBedSize(BedSize.SINGLE).get()))
                .price(BigDecimal.valueOf(678.32))
                .floor(3)
                .build();

        roomToDelete = roomRepository.save(roomToDelete);

        mvc.perform(delete(RestApiRoutes.SYSTEM_DELETE_ROOM, roomToDelete.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteRoomBadRequest() throws Exception {
        mvc.perform(delete(RestApiRoutes.SYSTEM_DELETE_ROOM, "123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteRoomNotFound() throws Exception {
        mvc.perform(delete(RestApiRoutes.SYSTEM_DELETE_ROOM, UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
