package com.tinqinacademy.hotel.core.conversion.user;

import com.tinqinacademy.hotel.api.operations.hotel.bookroom.BookRoomInput;
import com.tinqinacademy.hotel.core.conversion.BaseConverter;
import com.tinqinacademy.hotel.persistence.model.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class BookRoomInputToUserConverter extends BaseConverter<BookRoomInput, User> {
    @Override
    public User convertObject(BookRoomInput source) {
        //TODO: Use real data
        User user = User.builder()
                .username("exampleUsername") // missing information
                .phoneNumber(source.getPhoneNumber())
                .firstName(source.getFirstName())
                .lastName(source.getLastName())
                .email("example@example.com") // missing information
                .dateOfBirth(LocalDate.now())
                .password("password") // missing information
                .build();
        return user;
    }
}
