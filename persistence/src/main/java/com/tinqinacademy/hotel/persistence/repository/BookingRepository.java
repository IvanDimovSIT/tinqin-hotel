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
    @Query("SELECT b " +
            "FROM Booking b " +
            "JOIN b.guests g " +
            "JOIN b.room r " +
            "JOIN b.user u " +
            "WHERE (:startDate IS NULL OR b.startDate = :startDate) " +
            "AND (:endDate IS NULL OR b.endDate = :endDate) " +
            "AND (:firstName IS NULL OR g.firstName LIKE :firstName) " +
            "AND (:lastName IS NULL OR g.lastName LIKE :lastName) " +
            "AND (:phoneNumber IS NULL OR u.phoneNumber = :phoneNumber) " +
            "AND (:idCardNumber IS NULL OR g.idCardNo = :idCardNumber) " +
            "AND (:idCardValidity IS NULL OR g.idCardValidity = :idCardValidity) " +
            "AND (:idCardIssueAuthority IS NULL OR g.idCardIssueAuthority LIKE :idCardIssueAuthority) " +
            "AND (:idCardIssueDate IS NULL OR g.idCardIssueDate = :idCardIssueDate) " +
            "AND (:roomNumber IS NULL OR r.roomNo = :roomNumber)")
    List<Booking> findBookingsByCriteria(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("phoneNumber") String phoneNumber,
            @Param("idCardNumber") String idCardNumber,
            @Param("idCardValidity") LocalDate idCardValidity,
            @Param("idCardIssueAuthority") String idCardIssueAuthority,
            @Param("idCardIssueDate") LocalDate idCardIssueDate,
            @Param("roomNumber") String roomNumber);


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
