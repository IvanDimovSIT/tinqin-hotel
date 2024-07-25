package com.tinqinacademy.hotel.api.services;


import com.tinqinacademy.hotel.api.operations.hotel.bookroom.BookRoomInput;
import com.tinqinacademy.hotel.api.operations.hotel.bookroom.BookRoomOutput;
import com.tinqinacademy.hotel.api.operations.hotel.checkavailablerooms.CheckAvailableRoomsInput;
import com.tinqinacademy.hotel.api.operations.hotel.checkavailablerooms.CheckAvailableRoomsOutput;
import com.tinqinacademy.hotel.api.operations.hotel.getroom.GetRoomInput;
import com.tinqinacademy.hotel.api.operations.hotel.getroom.GetRoomOutput;
import com.tinqinacademy.hotel.api.operations.hotel.unbookroom.UnbookRoomInput;
import com.tinqinacademy.hotel.api.operations.hotel.unbookroom.UnbookRoomOutput;

public interface HotelService {
    CheckAvailableRoomsOutput checkAvailableRooms(final CheckAvailableRoomsInput input);
    GetRoomOutput getRoom(final GetRoomInput input);
    BookRoomOutput bookRoom(final BookRoomInput input);
    UnbookRoomOutput unbookRoom(final UnbookRoomInput input);
}
