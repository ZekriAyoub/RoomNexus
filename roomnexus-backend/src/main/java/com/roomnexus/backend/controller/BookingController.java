package com.roomnexus.backend.controller;

import com.roomnexus.backend.dto.BookingResponse;
import com.roomnexus.backend.dto.CreateBookingRequest;
import com.roomnexus.backend.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Bookings", description = "Gestion des réservations")
public class BookingController {

    private final BookingService bookingService;

    @Operation(summary = "Créer une réservation")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public BookingResponse createBooking(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid CreateBookingRequest request) {
        return bookingService.createBooking(jwt, request);
    }

    @Operation(summary = "Mes réservations")
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<BookingResponse> getMyBookings(@AuthenticationPrincipal Jwt jwt) {
        return bookingService.getMyBookings(jwt);
    }

    @Operation(summary = "Réservations d'une salle")
    @GetMapping("/room/{roomId}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<BookingResponse> getBookingsByRoom(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID roomId) {
        return bookingService.getBookingsByRoom(jwt, roomId);
    }

    @Operation(summary = "Annuler une réservation")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void cancelBooking(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID id) {
        bookingService.cancelBooking(jwt, id);
    }
}