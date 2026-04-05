package com.roomnexus.backend.controller;

import com.roomnexus.backend.dto.BookingResponse;
import com.roomnexus.backend.dto.CreateBookingRequest;
import com.roomnexus.backend.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public BookingResponse createBooking(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid CreateBookingRequest request) {
        return bookingService.createBooking(jwt, request);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<BookingResponse> getMyBookings(@AuthenticationPrincipal Jwt jwt) {
        return bookingService.getMyBookings(jwt);
    }

    @GetMapping("/room/{roomId}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<BookingResponse> getBookingsByRoom(@PathVariable UUID roomId) {
        return bookingService.getBookingsByRoom(roomId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void cancelBooking(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID id) {
        bookingService.cancelBooking(jwt, id);
    }
}