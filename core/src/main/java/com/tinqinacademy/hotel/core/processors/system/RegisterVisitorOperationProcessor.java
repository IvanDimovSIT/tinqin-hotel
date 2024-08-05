package com.tinqinacademy.hotel.core.processors.system;

import com.tinqinacademy.hotel.api.errors.Errors;
import com.tinqinacademy.hotel.api.model.visitor.VisitorInput;
import com.tinqinacademy.hotel.api.operations.system.registervisitor.RegisterVisitorInput;
import com.tinqinacademy.hotel.api.operations.system.registervisitor.RegisterVisitorOutput;
import com.tinqinacademy.hotel.api.operations.system.registervisitor.RegisterVisitorOperation;
import com.tinqinacademy.hotel.core.errors.ErrorMapper;
import com.tinqinacademy.hotel.api.exception.exceptions.NotFoundException;
import com.tinqinacademy.hotel.api.exception.exceptions.RegisterVisitorException;
import com.tinqinacademy.hotel.core.processors.BaseOperationProcessor;
import com.tinqinacademy.hotel.persistence.model.Booking;
import com.tinqinacademy.hotel.persistence.model.Guest;
import com.tinqinacademy.hotel.persistence.repository.BookingRepository;
import com.tinqinacademy.hotel.persistence.repository.GuestRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class RegisterVisitorOperationProcessor extends BaseOperationProcessor implements RegisterVisitorOperation {
    private final BookingRepository bookingRepository;
    private final GuestRepository guestRepository;

    public RegisterVisitorOperationProcessor(ConversionService conversionService, ErrorMapper errorMapper,
                                             Validator validator, BookingRepository bookingRepository,
                                             GuestRepository guestRepository) {
        super(conversionService, errorMapper, validator);
        this.bookingRepository = bookingRepository;
        this.guestRepository = guestRepository;
    }


    private boolean checkCardInvalid(VisitorInput visitor) {
        return !(visitor.getIdCardNumber() != null && visitor.getIdCardIssueDate() != null &&
                visitor.getIdCardIssueAuthority() != null && visitor.getIdCardValidity() != null) &&
                !(visitor.getIdCardNumber() == null && visitor.getIdCardIssueDate() == null &&
                        visitor.getIdCardIssueAuthority() == null && visitor.getIdCardValidity() == null);
    }

    private Booking findBookingForVisitor(VisitorInput visitor) {
        Booking booking = bookingRepository.findByRoomIdAndStartDateAndEndDate(
                        UUID.fromString(visitor.getRoomId()),
                        visitor.getStartDate(),
                        visitor.getEndDate())
                .orElseThrow(
                        () -> new NotFoundException(
                                "Booking with roomId:" + visitor.getRoomId() +
                                        " start date:" + visitor.getStartDate() +
                                        " end date:" + visitor.getEndDate()));

        return booking;
    }

    private Guest saveVisitor(VisitorInput visitor) {
        if (checkCardInvalid(visitor)) {
            throw new RegisterVisitorException("Error registering visitor: invalid id information");
        }

        Guest guest = conversionService.convert(visitor, Guest.class);
        guest = guestRepository.save(guest);
        Booking booking = findBookingForVisitor(visitor);
        booking.getGuests().add(guest);
        bookingRepository.save(booking);

        return guest;
    }

    @Override
    public Either<Errors, RegisterVisitorOutput> process(RegisterVisitorInput input) {
        return Try.of(() -> {
                    log.info("Start registerVisitor input:{}", input);
                    validate(input);
                    List<Guest> guests = input.getVisitorInputs().stream()
                            .map(this::saveVisitor)
                            .toList();

                    log.info("registerVisitor created guests:{}", guests);

                    RegisterVisitorOutput result = RegisterVisitorOutput.builder()
                            .build();
                    log.info("End registerVisitor result:{}", result);
                    return result;
                })
                .toEither()
                .mapLeft(errorMapper::map);
    }
}
