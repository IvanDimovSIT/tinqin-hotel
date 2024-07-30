package com.tinqinacademy.hotel.core.exception.exceptions;

public class DeleteRoomException extends RuntimeException {
    public DeleteRoomException(String roomId) {
        super("Failed to delete room with id:" + roomId);
    }
}
