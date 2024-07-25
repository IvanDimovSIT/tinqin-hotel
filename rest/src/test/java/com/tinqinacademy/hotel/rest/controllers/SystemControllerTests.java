package com.tinqinacademy.hotel.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinqinacademy.hotel.api.model.enums.BathroomType;
import com.tinqinacademy.hotel.api.model.enums.BedSize;
import com.tinqinacademy.hotel.api.model.visitor.VisitorInput;
import com.tinqinacademy.hotel.api.operations.system.addroom.AddRoomInput;
import com.tinqinacademy.hotel.api.operations.system.partialupdateroom.PartialUpdateRoomInput;
import com.tinqinacademy.hotel.api.operations.system.registervisitor.RegisterVisitorInput;
import com.tinqinacademy.hotel.api.operations.system.updateroom.UpdateRoomInput;
import com.tinqinacademy.hotel.api.RestApiRoutes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SystemControllerTests {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void testRegisterVisitorBadRequest() throws Exception {

        RegisterVisitorInput input = RegisterVisitorInput.builder()
                .visitorInputs(null)
                .build();

        String serializedInput = mapper.writeValueAsString(input);

        mvc.perform(post(RestApiRoutes.SYSTEM_REGISTER_VISITOR)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializedInput))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testRegisterVisitorOk() throws Exception {
        VisitorInput visitorInput = VisitorInput.builder()
                .build();

        RegisterVisitorInput input = RegisterVisitorInput.builder()
                .visitorInputs(List.of(visitorInput))
                .build();

        String serializedInput = mapper.writeValueAsString(input);

        mvc.perform(post(RestApiRoutes.SYSTEM_REGISTER_VISITOR)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializedInput))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetVisitorsOk() throws Exception {
        mvc.perform(get(RestApiRoutes.SYSTEM_GET_VISITORS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("startDate", LocalDate.now().toString())
                        .param("endDate", LocalDate.now().toString())
                        .param("firstName", "Ivan")
                        .param("lastName", "Dragan")
                        .param("phoneNumber", "0987654321")
                        .param("idCardNumber", "123")
                        .param("idCardValidity", LocalDate.now().toString())
                        .param("idCardIssueAuthority", "Abc")
                        .param("idCardIssueDate", LocalDate.now().toString())
                        .param("roomNumber", "123"))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddRoomOk() throws Exception {
        AddRoomInput input = AddRoomInput.builder()
                .roomNumber("123")
                .floor(1)
                .bathroomType(BathroomType.PRIVATE)
                .price(BigDecimal.valueOf(1000))
                .bedCount(2)
                .bedSize(BedSize.KING_SIZE)
                .build();

        String serializedInput = mapper.writeValueAsString(input);

        mvc.perform(post(RestApiRoutes.SYSTEM_ADD_ROOM)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializedInput))
                .andExpect(status().isCreated());
    }

    @Test
    public void testAddRoomBadRequest() throws Exception {
        AddRoomInput input = AddRoomInput.builder()
                .roomNumber("123")
                .floor(1)
                .bathroomType(BathroomType.PRIVATE)
                .price(BigDecimal.valueOf(-9999))
                .bedCount(0)
                .bedSize(BedSize.KING_SIZE)
                .build();

        String serializedInput = mapper.writeValueAsString(input);

        mvc.perform(post(RestApiRoutes.SYSTEM_ADD_ROOM)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializedInput))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateRoomOk() throws Exception {
        UpdateRoomInput input = UpdateRoomInput.builder()
                .roomNumber("123")
                .floor(1)
                .bathroomType(BathroomType.PRIVATE)
                .price(BigDecimal.valueOf(1000))
                .bedCount(2)
                .bedSize(BedSize.KING_SIZE)
                .build();

        String serializedInput = mapper.writeValueAsString(input);

        mvc.perform(put(RestApiRoutes.SYSTEM_UPDATE_ROOM, "123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializedInput))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateRoomBadRequest() throws Exception {
        UpdateRoomInput input = UpdateRoomInput.builder()
                .roomNumber("123")
                .floor(1)
                .bathroomType(BathroomType.PRIVATE)
                .price(BigDecimal.valueOf(1000))
                .bedCount(999999)
                .bedSize(BedSize.KING_SIZE)
                .build();

        String serializedInput = mapper.writeValueAsString(input);

        mvc.perform(put(RestApiRoutes.SYSTEM_UPDATE_ROOM, "123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializedInput))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testPartialUpdateRoomOk() throws Exception {
        PartialUpdateRoomInput input = PartialUpdateRoomInput.builder()
                .roomNumber("123")
                .floor(1)
                .bathroomType(BathroomType.PRIVATE)
                .price(BigDecimal.valueOf(1000))
                .bedCount(2)
                .bedSize(BedSize.KING_SIZE)
                .build();

        String serializedInput = mapper.writeValueAsString(input);

        mvc.perform(patch(RestApiRoutes.SYSTEM_PARTIAL_UPDATE_ROOM, "123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializedInput))
                .andExpect(status().isOk());
    }

    @Test
    public void testPartialUpdateRoomBadRequest() throws Exception {
        PartialUpdateRoomInput input = PartialUpdateRoomInput.builder()
                .roomNumber("123")
                .floor(-12)
                .bathroomType(BathroomType.PRIVATE)
                .price(BigDecimal.valueOf(1000))
                .bedCount(2)
                .bedSize(BedSize.KING_SIZE)
                .build();

        String serializedInput = mapper.writeValueAsString(input);

        mvc.perform(patch(RestApiRoutes.SYSTEM_PARTIAL_UPDATE_ROOM, "123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializedInput))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteRoomOk() throws Exception {
        mvc.perform(delete(RestApiRoutes.SYSTEM_DELETE_ROOM, "123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
