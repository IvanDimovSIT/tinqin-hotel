package com.tinqinacademy.hotel.core.services.system;

import com.tinqinacademy.hotel.api.errors.Errors;
import com.tinqinacademy.hotel.api.model.visitor.VisitorOutput;
import com.tinqinacademy.hotel.api.operations.system.getvisitors.GetVisitorsInput;
import com.tinqinacademy.hotel.api.operations.system.getvisitors.GetVisitorsOutput;
import com.tinqinacademy.hotel.api.operations.system.getvisitors.GetVisitorsOperation;
import com.tinqinacademy.hotel.persistence.model.Booking;
import com.tinqinacademy.hotel.persistence.model.Guest;
import com.tinqinacademy.hotel.persistence.model.Room;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetVisitorsOperationProcessor implements GetVisitorsOperation {
    private final ConversionService conversionService;
    private final EntityManager entityManager;

    private <T> void addPredicateIfPresent(List<Predicate> predicates, Predicate predicate, T field) {
        if (field != null) {
            predicates.add(predicate);
        }
    }

    private List<Tuple> findVisitors(GetVisitorsInput input) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = criteriaBuilder.createTupleQuery();
        Root<Booking> bookingRoot = query.from(Booking.class);
        Join<Booking, Guest> guestJoin = bookingRoot.join("guests");
        Join<Booking, Room> roomJoin = bookingRoot.join("room");

        List<Predicate> predicates = new ArrayList<>();

        Predicate startDatePredicate = criteriaBuilder.between(
                bookingRoot.get("startDate"), input.getStartDate(), input.getEndDate());
        Predicate endDatePredicate = criteriaBuilder.between(
                bookingRoot.get("endDate"), input.getStartDate(), input.getEndDate());
        Predicate overlapStartDatePredicate = criteriaBuilder.lessThanOrEqualTo(
                bookingRoot.get("startDate"), input.getStartDate());
        Predicate overlapEndDatePredicate = criteriaBuilder.greaterThanOrEqualTo(
                bookingRoot.get("endDate"), input.getEndDate());

        predicates.add(criteriaBuilder.or(startDatePredicate, endDatePredicate,
                criteriaBuilder.and(overlapStartDatePredicate, overlapEndDatePredicate)));

        addPredicateIfPresent(predicates, criteriaBuilder.like(guestJoin.get("firstName"), input.getFirstName()), input.getFirstName());
        addPredicateIfPresent(predicates, criteriaBuilder.like(guestJoin.get("lastName"), input.getLastName()), input.getLastName());
        addPredicateIfPresent(predicates, criteriaBuilder.like(guestJoin.get("phoneNumber"), input.getPhoneNumber()), input.getPhoneNumber());
        addPredicateIfPresent(predicates, criteriaBuilder.equal(guestJoin.get("idCardNo"), input.getIdCardNumber()), input.getIdCardNumber());
        addPredicateIfPresent(predicates, criteriaBuilder.equal(guestJoin.get("idCardValidity"), input.getIdCardValidity()), input.getIdCardValidity());
        addPredicateIfPresent(predicates, criteriaBuilder.equal(guestJoin.get("idCardIssueAuthority"), input.getIdCardIssueAuthority()), input.getIdCardIssueAuthority());
        addPredicateIfPresent(predicates, criteriaBuilder.equal(guestJoin.get("idCardIssueDate"), input.getIdCardIssueDate()), input.getIdCardIssueDate());
        addPredicateIfPresent(predicates, criteriaBuilder.like(roomJoin.get("roomNo"), input.getRoomNumber()), input.getRoomNumber());

        query.where(predicates.toArray(new Predicate[0]));
        query.multiselect(guestJoin, bookingRoot);

        return entityManager.createQuery(query).getResultList();
    }

    private List<VisitorOutput> mapVisitors(List<Tuple> foundGuestsBookings) {
        return foundGuestsBookings.stream()
                .map(tuple -> {
                    Guest guest = tuple.get(0, Guest.class);
                    Booking booking = tuple.get(1, Booking.class);
                    VisitorOutput visitorOutput = conversionService.convert(guest, VisitorOutput.class);
                    if (visitorOutput != null) {
                        visitorOutput.setRoomId(booking.getRoom().getId().toString());
                        visitorOutput.setStartDate(booking.getStartDate());
                        visitorOutput.setEndDate(booking.getEndDate());
                    }

                    return visitorOutput;
                })
                .toList();
    }

    @Override
    public Either<Errors, GetVisitorsOutput> process(GetVisitorsInput input) {
        log.info("Start getVisitors input:{}", input);

        Either<Errors, GetVisitorsOutput> result = Try.of(() -> {
                    List<Tuple> results = findVisitors(input);
                    List<VisitorOutput> visitorOutputs = mapVisitors(results);

                    return GetVisitorsOutput.builder()
                            .visitorOutputs(visitorOutputs)
                            .build();
                })
                .toEither()
                .mapLeft(throwable -> Match(throwable).of(
                        Case($(), Errors.builder()
                                .error(throwable.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR)
                                .build()
                        )
                ));

        log.info("End getVisitors result:{}", result);

        return result;
    }
}
