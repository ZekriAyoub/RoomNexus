package com.roomnexus.backend.service;

import com.roomnexus.backend.dto.BookingResponse;
import com.roomnexus.backend.dto.CreateBookingRequest;
import com.roomnexus.backend.entity.Booking;
import com.roomnexus.backend.entity.Room;
import com.roomnexus.backend.entity.UserProfile;
import com.roomnexus.backend.exception.CompanyAccessGuard;
import com.roomnexus.backend.repository.BookingRepository;
import com.roomnexus.backend.repository.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserProfileService userProfileService;

    public BookingResponse createBooking(Jwt jwt, CreateBookingRequest request) {
        validateBookingDates(request);

        UserProfile user = userProfileService.getActiveUserProfile(jwt);

        Room room = roomRepository.findById(request.roomId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Room not found with id: " + request.roomId()));

        CompanyAccessGuard.verify(user, room.getCompany().getId());

        validateRoomAvailability(room);
        validateNoOverlap(request);

        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setBookedBy(user);
        booking.setStartTime(request.startTime());
        booking.setEndTime(request.endTime());

        BookingResponse response = toResponse(bookingRepository.save(booking));
        log.info("Booking created: id={} room={} user={} from={} to={}",
                response.id(),
                response.roomId(),
                response.bookedById(),
                response.startTime(),
                response.endTime());
        return response;
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getMyBookings(Jwt jwt) {
        UserProfile user = userProfileService.getActiveUserProfile(jwt);
        return bookingRepository.findByBookedById(user.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getBookingsByRoom(Jwt jwt, UUID roomId) {
        UserProfile user = userProfileService.getActiveUserProfile(jwt);
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Room not found with id: " + roomId));

        CompanyAccessGuard.verify(user, room.getCompany().getId());

        return bookingRepository.findByRoomId(roomId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public void cancelBooking(Jwt jwt, UUID bookingId) {
        UserProfile user = userProfileService.getActiveUserProfile(jwt);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Booking not found with id: " + bookingId));

        if (!booking.getBookedBy().getId().equals(user.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not allowed to cancel this booking");
        }

        CompanyAccessGuard.verify(user, booking.getRoom().getCompany().getId());

        log.info("Booking cancelled: id={} by user={}",
                bookingId,
                user.getId());
        bookingRepository.delete(booking);
    }

    private void validateBookingDates(CreateBookingRequest request) {
        if (!request.startTime().isBefore(request.endTime())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Start time must be before end time");
        }
    }

    private void validateRoomAvailability(Room room) {
        if (!room.isAvailable()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Room is not available: " + room.getId());
        }
    }

    private void validateNoOverlap(CreateBookingRequest request) {
        boolean hasOverlap = bookingRepository.existsOverlappingBooking(
                request.roomId(),
                request.startTime(),
                request.endTime()
        );
        if (hasOverlap) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Room is already booked for the requested time slot");
        }
    }

    private BookingResponse toResponse(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getRoom().getId(),
                booking.getRoom().getName(),
                booking.getBookedBy().getId(),
                booking.getBookedBy().getFirstName(),
                booking.getBookedBy().getLastName(),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getCreatedAt()
        );
    }
}