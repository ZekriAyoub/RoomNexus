package com.roomnexus.backend.controller;

import com.roomnexus.backend.dto.CreateRoomRequest;
import com.roomnexus.backend.dto.RoomResponse;
import com.roomnexus.backend.dto.UpdateRoomRequest;
import com.roomnexus.backend.service.RoomService;
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
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
@Tag(name = "Rooms", description = "Room management")
public class RoomController {

    private final RoomService roomService;

    @Operation(summary = "Create a room",
            description = "Creates a new room for the specified company. Restricted to ADMIN")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public RoomResponse createRoom(@RequestBody @Valid CreateRoomRequest request) {
        return roomService.createRoom(request);
    }

    @Operation(summary = "Get all rooms by company")
    @GetMapping("/company/{companyId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<RoomResponse> getRoomsByCompany(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID companyId) {
        return roomService.getRoomsByCompany(jwt, companyId);
    }

    @Operation(summary = "Get available rooms by company")
    @GetMapping("/company/{companyId}/available")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<RoomResponse> getAvailableRoomsByCompany(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID companyId) {
        return roomService.getAvailableRoomsByCompany(jwt, companyId);
    }

    @Operation(summary = "Get room by ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public RoomResponse getRoomById(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID id) {
        return roomService.getRoomById(jwt, id);
    }

    @Operation(summary = "Update a room",
            description = "Updates room details including availability. Restricted to ADMIN")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public RoomResponse updateRoom(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID id,
            @RequestBody @Valid UpdateRoomRequest request) {
        return roomService.updateRoom(jwt, id, request);
    }

    @Operation(summary = "Delete a room")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteRoom(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID id) {
        roomService.deleteRoom(jwt, id);
    }
}