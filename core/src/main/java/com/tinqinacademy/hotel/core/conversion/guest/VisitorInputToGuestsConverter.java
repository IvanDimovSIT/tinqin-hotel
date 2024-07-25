package com.tinqinacademy.hotel.core.conversion.guest;

import com.tinqinacademy.hotel.api.model.visitor.VisitorInput;
import com.tinqinacademy.hotel.api.operations.system.registervisitor.RegisterVisitorInput;
import com.tinqinacademy.hotel.core.conversion.BaseConverter;
import com.tinqinacademy.hotel.persistence.model.Guest;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class VisitorInputToGuestsConverter extends BaseConverter<VisitorInput, Guest> {

    @Override
    protected Guest convertObject(VisitorInput source) {
        Guest guest = Guest.builder()
                .firstName(source.getFirstName())
                .lastName(source.getLastName())
                .dateOfBirth(source.getDateOfBirth())
                .idCardNo(source.getIdCardNumber())
                .idCardIssueDate(source.getIdCardIssueDate())
                .idCardValidity(source.getIdCardValidity())
                .idCardIssueAuthority(source.getIdCardIssueAuthority())
                .build();


        return guest;
    }
}
