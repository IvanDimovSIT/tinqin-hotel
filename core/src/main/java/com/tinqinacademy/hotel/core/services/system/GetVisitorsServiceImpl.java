package com.tinqinacademy.hotel.core.services.system;

import com.tinqinacademy.hotel.api.model.visitor.VisitorOutput;
import com.tinqinacademy.hotel.api.operations.system.getvisitors.GetVisitorsInput;
import com.tinqinacademy.hotel.api.operations.system.getvisitors.GetVisitorsOutput;
import com.tinqinacademy.hotel.api.operations.system.getvisitors.GetVisitorsService;
import com.tinqinacademy.hotel.persistence.model.Booking;
import com.tinqinacademy.hotel.persistence.model.Guest;
import com.tinqinacademy.hotel.persistence.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetVisitorsServiceImpl implements GetVisitorsService {
    private final BookingRepository bookingRepository;
    private final ConversionService conversionService;

    @Override
    public GetVisitorsOutput process(GetVisitorsInput input) {
        log.info("Start getVisitors input:{}", input);

        List<Booking> bookings = bookingRepository.findBookingsByCriteria(
                input.getStartDate(),
                input.getEndDate(),
                input.getFirstName(),
                input.getLastName(),
                input.getPhoneNumber(),
                input.getIdCardNumber(),
                input.getIdCardValidity(),
                input.getIdCardIssueAuthority(),
                input.getIdCardIssueDate(),
                input.getRoomNumber());

        List<VisitorOutput> visitorOutputs = new ArrayList<>();
        for (Booking booking : bookings) {
            for (Guest guest : booking.getGuests()) {
                if (input.getFirstName() != null && !input.getFirstName().equals(guest.getFirstName())) {
                    continue;
                }
                if (input.getLastName() != null && !input.getLastName().equals(guest.getLastName())) {
                    continue;
                }
                if (input.getIdCardNumber() != null && !input.getIdCardNumber().equals(guest.getIdCardNo())) {
                    continue;
                }
                if (input.getIdCardValidity() != null && !input.getIdCardValidity().equals(guest.getIdCardValidity())) {
                    continue;
                }
                if (input.getIdCardIssueAuthority() != null && !input.getIdCardIssueAuthority().equals(guest.getIdCardIssueAuthority())) {
                    continue;
                }
                if (input.getIdCardIssueDate() != null && !input.getIdCardIssueDate().equals(guest.getIdCardIssueDate())) {
                    continue;
                }
                if (input.getPhoneNumber() != null && !input.getPhoneNumber().equals(guest.getPhoneNumber())) {
                    continue;
                }
                VisitorOutput visitorOutput = conversionService.convert(guest, VisitorOutput.class);
                visitorOutput.setRoomId(booking.getRoom().getId().toString());
                visitorOutput.setStartDate(booking.getStartDate());
                visitorOutput.setEndDate(booking.getEndDate());

                visitorOutputs.add(visitorOutput);
            }
        }

        GetVisitorsOutput result = GetVisitorsOutput.builder()
                .visitorOutputs(visitorOutputs)
                .build();

        log.info("End getVisitors result:{}", result);

        return result;
    }
}
