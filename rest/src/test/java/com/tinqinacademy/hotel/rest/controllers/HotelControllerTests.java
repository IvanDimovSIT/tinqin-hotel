package com.tinqinacademy.hotel.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinqinacademy.hotel.api.RestApiRoutes;
import com.tinqinacademy.hotel.api.operations.hotel.bookroom.BookRoomInput;
import com.tinqinacademy.hotel.api.operations.hotel.unbookroom.UnbookRoomInput;
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
public class HotelControllerTests {
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
        bookingRepository.deleteAll();
        roomRepository.deleteAll();
    }

    @Test
    public void testCheckAvailableRoomsOk() throws Exception {
        mvc.perform(get(RestApiRoutes.HOTEL_GET_AVAILABLE_ROOMS)
                        .param("startDate", "2029-08-20")
                        .param("endDate", "2029-08-25")
                        .param("bedCount", "1")
                        .param("bedSize", "single")
                        .param("bathroomType", "shared")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testCheckAvailableRoomsBadRequest() throws Exception {
        mvc.perform(get(RestApiRoutes.HOTEL_GET_AVAILABLE_ROOMS)
                        .param("startDate", "2029-08-20")
                        .param("endDate", "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetRoomOk() throws Exception {
        Room room = roomRepository.findAll().getFirst();

        mvc.perform(get(RestApiRoutes.HOTEL_GET_ROOM, room.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetRoomNotFound() throws Exception {
        mvc.perform(get(RestApiRoutes.HOTEL_GET_ROOM, UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetRoomBadRequest() throws Exception {
        mvc.perform(get(RestApiRoutes.HOTEL_GET_ROOM, "123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testBookRoomCreated() throws Exception {
        Room room = roomRepository.findAll().getFirst();

        BookRoomInput input = BookRoomInput.builder()
                .userId(UUID.randomUUID().toString())
                .startDate(LocalDate.of(2029, 8, 20))
                .endDate(LocalDate.of(2029, 8, 25))
                .build();

        mvc.perform(post(RestApiRoutes.HOTEL_BOOK_ROOM, room.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(input)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testBookRoomBadRequestBookingInThePast() throws Exception {
        Room room = roomRepository.findAll().getFirst();

        BookRoomInput input = BookRoomInput.builder()
                .userId(UUID.randomUUID().toString())
                .startDate(LocalDate.of(2004, 8, 20))
                .endDate(LocalDate.of(2004, 8, 25))
                .build();

        mvc.perform(post(RestApiRoutes.HOTEL_BOOK_ROOM, room.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testBookRoomAlreadyBooked() throws Exception {
        Booking booking = bookingRepository.findAll().getFirst();

        BookRoomInput input = BookRoomInput.builder()
                .userId(UUID.randomUUID().toString())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .build();

        mvc.perform(post(RestApiRoutes.HOTEL_BOOK_ROOM, booking.getRoom().getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testBookRoomNotFound() throws Exception {
        BookRoomInput input = BookRoomInput.builder()
                .userId(UUID.randomUUID().toString())
                .startDate(LocalDate.of(2029, 8, 20))
                .endDate(LocalDate.of(2029, 8, 25))
                .build();

        mvc.perform(post(RestApiRoutes.HOTEL_BOOK_ROOM, UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(input)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testBookRoomBadRequestInvalidRoomId() throws Exception {
        BookRoomInput input = BookRoomInput.builder()
                .userId(UUID.randomUUID().toString())
                .startDate(LocalDate.of(2029, 8, 20))
                .endDate(LocalDate.of(2029, 8, 25))
                .build();

        mvc.perform(post(RestApiRoutes.HOTEL_BOOK_ROOM, "234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testBookRoomBadRequestInvalidRequestBody() throws Exception {
        Room room = roomRepository.findAll().getFirst();

        BookRoomInput input = BookRoomInput.builder()
                .userId(UUID.randomUUID().toString())
                .startDate(LocalDate.of(2029, 8, 20))
                .build();

        mvc.perform(post(RestApiRoutes.HOTEL_BOOK_ROOM, room.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUnbookRoomOk() throws Exception {
        Booking booking = bookingRepository.findAll().getFirst();

        UnbookRoomInput input = UnbookRoomInput.builder()
                .userId(booking.getUserId().toString())
                .build();

        mvc.perform(delete(RestApiRoutes.HOTEL_UNBOOK_ROOM, booking.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(input)))
                .andExpect(status().isOk());
    }

    @Test
    public void testUnbookRoomBadRequestUserDoesntOwnBooking() throws Exception {
        Booking booking = bookingRepository.findAll().getFirst();

        UnbookRoomInput input = UnbookRoomInput.builder()
                .userId(UUID.randomUUID().toString())
                .build();

        mvc.perform(delete(RestApiRoutes.HOTEL_UNBOOK_ROOM, booking.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUnbookRoomBadRequestInvalidBody() throws Exception {
        Booking booking = bookingRepository.findAll().getFirst();

        UnbookRoomInput input = UnbookRoomInput.builder()
                .build();

        mvc.perform(delete(RestApiRoutes.HOTEL_UNBOOK_ROOM, booking.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUnbookRoomBadRequestInvalidRoomId() throws Exception {
        Booking booking = bookingRepository.findAll().getFirst();

        UnbookRoomInput input = UnbookRoomInput.builder()
                .userId(booking.getUserId().toString())
                .build();

        mvc.perform(delete(RestApiRoutes.HOTEL_UNBOOK_ROOM, "123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUnbookRoomNotFound() throws Exception {
        Booking booking = bookingRepository.findAll().getFirst();
        UnbookRoomInput input = UnbookRoomInput.builder()
                .userId(booking.getUserId().toString())
                .build();

        mvc.perform(delete(RestApiRoutes.HOTEL_UNBOOK_ROOM, UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(input)))
                .andExpect(status().isNotFound());
    }
}
