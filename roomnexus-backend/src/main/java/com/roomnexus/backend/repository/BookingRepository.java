package com.roomnexus.backend.repository;

import com.roomnexus.backend.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {

    List<Booking> findByBookedById(UUID userProfileId);

    List<Booking> findByRoomId(UUID roomId);

    @Query("""
            SELECT COUNT(b) > 0 FROM Booking b
            WHERE b.room.id = :roomId
            AND b.startTime < :endTime
            AND b.endTime > :startTime
            """)
    boolean existsOverlappingBooking(
            @Param("roomId") UUID roomId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    //to update booking later
    @Query("""
            SELECT COUNT(b) > 0 FROM Booking b
            WHERE b.room.id = :roomId
            AND b.id <> :excludedBookingId
            AND b.startTime < :endTime
            AND b.endTime > :startTime
            """)
    boolean existsOverlappingBookingExcluding(
            @Param("roomId") UUID roomId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("excludedBookingId") UUID excludedBookingId
    );
}