package com.tinqinacademy.hotel.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinqinacademy.hotel.api.operations.hotel.bookroom.BookRoomInput;
import com.tinqinacademy.hotel.api.operations.hotel.unbookroom.UnbookRoomInput;
import com.tinqinacademy.hotel.rest.HotelApplication;
import com.tinqinacademy.hotel.api.RestApiRoutes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class HotelControllerTests {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void testGetRoomOk() throws Exception {
        mvc.perform(get(RestApiRoutes.HOTEL_GET_ROOM, 3)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAvailableRoomsOk() throws Exception {
        mvc.perform(get(RestApiRoutes.HOTEL_GET_AVAILABLE_ROOMS)
                        .param("startDate", LocalDate.now().toString())
                        .param("endDate", LocalDate.now().toString())
                        .param("bedCount", "2")
                        .param("bedSize", "kingSize")
                        .param("bathroomType", "shared")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testBookRoomsOk() throws Exception {
        BookRoomInput input = BookRoomInput.builder()
                .startDate(LocalDate.now())
                .endDate(LocalDate.now())
                .firstName("Ivan")
                .lastName("Dimov")
                .phoneNumber("0987654321")
                .build();

        String serializedInput = mapper.writeValueAsString(input);

        mvc.perform(post(RestApiRoutes.HOTEL_BOOK_ROOM, 3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializedInput))
                .andExpect(status().isCreated());
    }

    @Test
    public void testBookRoomsBadRequest() throws Exception {
        BookRoomInput input = BookRoomInput.builder()
                .startDate(LocalDate.now())
                .endDate(LocalDate.now())
                .firstName("Ivan")
                .lastName("Dimov")
                .phoneNumber("abc")
                .build();

        String serializedInput = mapper.writeValueAsString(input);

        mvc.perform(post(RestApiRoutes.HOTEL_BOOK_ROOM, 3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializedInput))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUnbookRoomsOk() throws Exception {
        mvc.perform(delete(RestApiRoutes.HOTEL_UNBOOK_ROOM, 3)
                        .contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().is2xxSuccessful());
    }
}