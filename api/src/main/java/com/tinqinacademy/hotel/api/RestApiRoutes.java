package com.tinqinacademy.hotel.api;

public class RestApiRoutes {
    public static final String API = "/api/v1";
    public static final String HOTEL_BASE =  API + "/hotel";
    public static final String SYSTEM_BASE = API + "/system";

    public static final String HOTEL_GET_AVAILABLE_ROOMS = HOTEL_BASE + "/rooms";
    public static final String HOTEL_GET_ROOM = HOTEL_BASE + "/{roomId}";
    public static final String HOTEL_BOOK_ROOM = HOTEL_BASE + "/{roomId}";
    public static final String HOTEL_UNBOOK_ROOM = HOTEL_BASE + "/{bookingId}";

    public static final String SYSTEM_REGISTER_VISITOR = SYSTEM_BASE + "/register";
    public static final String SYSTEM_GET_VISITORS = SYSTEM_BASE + "/register";
    public static final String SYSTEM_ADD_ROOM = SYSTEM_BASE + "/room";
    public static final String SYSTEM_UPDATE_ROOM = SYSTEM_BASE + "/room/{roomId}";
    public static final String SYSTEM_PARTIAL_UPDATE_ROOM = SYSTEM_BASE + "/room/{roomId}";
    public static final String SYSTEM_DELETE_ROOM = SYSTEM_BASE + "/room/{roomId}";
}
