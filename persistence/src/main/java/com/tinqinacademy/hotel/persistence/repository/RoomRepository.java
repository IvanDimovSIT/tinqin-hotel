package com.tinqinacademy.hotel.persistence.repository;

import com.tinqinacademy.hotel.persistence.model.Room;
import com.tinqinacademy.hotel.persistence.model.enums.BathroomType;
import com.tinqinacademy.hotel.persistence.model.enums.BedSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoomRepository extends JpaRepository<Room, UUID> {
    //todo: fix

    @Query("SELECT r FROM Room r " +
            "WHERE (:bathroomType IS NULL OR r.bathroomType = :bathroomType) AND " +
            "(:bedCount IS NULL OR :bedCount = SIZE(r.beds)) AND " +
            "(:bedSize IS NULL OR :bedSize IN (SELECT b.bedSize FROM r.beds b)) AND " +
            "r NOT IN (SELECT b.room FROM Booking b WHERE b.room = r AND " +
            "((b.startDate >= :startDate AND b.startDate <= :endDate) OR " +
            "(b.endDate >= :startDate AND b.endDate <= :endDate) OR " +
            "(b.startDate <= :startDate AND b.endDate >= :endDate)))")
    List<Room> findAvailableRooms(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("bathroomType") BathroomType bathroomType,
            @Param("bedSize") BedSize bedSize,
            @Param("bedCount") Integer bedCount);

    Optional<Room> findByRoomNo(String roomNo);
}
