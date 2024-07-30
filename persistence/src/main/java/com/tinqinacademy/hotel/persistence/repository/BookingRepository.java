package com.tinqinacademy.hotel.persistence.repository;

import com.tinqinacademy.hotel.persistence.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {

    List<Booking> findAllByRoomId(UUID roomId);

    Optional<Booking> findByRoomIdAndStartDateAndEndDate(UUID roomId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT EXISTS( SELECT b from Booking b WHERE b.room.id = :roomId AND " +
            "((b.startDate >= :startDate AND b.startDate <= :endDate) OR " +
            "(b.endDate >= :startDate AND b.endDate <= :endDate) OR " +
            "(b.startDate <= :startDate AND b.endDate >= :endDate)))")
    boolean checkRoomOccupied(
            @Param("roomId") UUID roomId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
