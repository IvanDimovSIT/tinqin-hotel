package com.tinqinacademy.hotel.core.services.system;

import com.tinqinacademy.hotel.api.model.visitor.VisitorOutput;
import com.tinqinacademy.hotel.api.operations.system.getvisitors.GetVisitorsInput;
import com.tinqinacademy.hotel.api.operations.system.getvisitors.GetVisitorsOutput;
import com.tinqinacademy.hotel.api.operations.system.getvisitors.GetVisitorsService;
import com.tinqinacademy.hotel.persistence.model.Booking;
import com.tinqinacademy.hotel.persistence.model.Guest;
import com.tinqinacademy.hotel.persistence.model.Room;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.*;
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
    private final ConversionService conversionService;
    private final EntityManager entityManager;

    @Override
    public GetVisitorsOutput process(GetVisitorsInput input) {
        log.info("Start getVisitors input:{}", input);

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

        if (input.getFirstName() != null) {
            predicates.add(criteriaBuilder.like(guestJoin.get("firstName"), input.getFirstName()));
        }
        if (input.getLastName() != null) {
            predicates.add(criteriaBuilder.like(guestJoin.get("lastName"), input.getLastName()));
        }
        if (input.getPhoneNumber() != null) {
            predicates.add(criteriaBuilder.equal(guestJoin.get("phoneNumber"), input.getPhoneNumber()));
        }
        if (input.getIdCardNumber() != null) {
            predicates.add(criteriaBuilder.equal(guestJoin.get("idCardNo"), input.getIdCardNumber()));
        }
        if (input.getIdCardValidity() != null) {
            predicates.add(criteriaBuilder.equal(guestJoin.get("idCardValidity"), input.getIdCardValidity()));
        }
        if (input.getIdCardIssueAuthority() != null) {
            predicates.add(criteriaBuilder.like(guestJoin.get("idCardIssueAuthority"), input.getIdCardIssueAuthority()));
        }
        if (input.getIdCardIssueDate() != null) {
            predicates.add(criteriaBuilder.equal(guestJoin.get("idCardIssueDate"), input.getIdCardIssueDate()));
        }
        if (input.getRoomNumber() != null) {
            predicates.add(criteriaBuilder.like(roomJoin.get("roomNo"), input.getRoomNumber()));
        }

        query.where(predicates.toArray(new Predicate[0]));
        query.multiselect(guestJoin, bookingRoot);

        List<Tuple> results = entityManager.createQuery(query).getResultList();
        List<VisitorOutput> visitorOutputs = results.stream()
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


        GetVisitorsOutput result = GetVisitorsOutput.builder()
                .visitorOutputs(visitorOutputs)
                .build();

        log.info("End getVisitors result:{}", result);

        return result;
    }
}
