package com.tinqinacademy.hotel.core.conversion.guest;

import com.tinqinacademy.hotel.api.model.visitor.VisitorOutput;
import com.tinqinacademy.hotel.core.conversion.BaseConverter;
import com.tinqinacademy.hotel.persistence.model.Guest;
import org.springframework.stereotype.Component;

@Component
public class GuestToVisitorOutputConverter extends BaseConverter<Guest, VisitorOutput.VisitorOutputBuilder> {

    @Override
    protected VisitorOutput.VisitorOutputBuilder convertObject(Guest source) {
        return VisitorOutput.builder()
                .firstName(source.getFirstName())
                .lastName(source.getLastName())
                .idCardNumber(source.getIdCardNo())
                .idCardIssueDate(source.getIdCardIssueDate())
                .idCardIssueAuthority(source.getIdCardIssueAuthority())
                .idCardValidity(source.getIdCardValidity())
                .phoneNumber(source.getPhoneNumber());
    }
}
