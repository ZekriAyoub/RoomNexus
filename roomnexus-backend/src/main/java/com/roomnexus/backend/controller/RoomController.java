package com.roomnexus.backend.controller;

import com.roomnexus.backend.dto.CreateRoomRequest;
import com.roomnexus.backend.dto.RoomResponse;
import com.roomnexus.backend.dto.UpdateRoomRequest;
import com.roomnexus.backend.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public RoomResponse createRoom(@RequestBody @Valid CreateRoomRequest request) {
        return roomService.createRoom(request);
    }

    @GetMapping("/company/{companyId}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<RoomResponse> getRoomsByCompany(@PathVariable UUID companyId) {
        return roomService.getRoomsByCompany(companyId);
    }

    @GetMapping("/company/{companyId}/available")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<RoomResponse> getAvailableRoomsByCompany(@PathVariable UUID companyId) {
        return roomService.getAvailableRoomsByCompany(companyId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public RoomResponse getRoomById(@PathVariable UUID id) {
        return roomService.getRoomById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public RoomResponse updateRoom(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateRoomRequest request) {
        return roomService.updateRoom(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteRoom(@PathVariable UUID id) {
        roomService.deleteRoom(id);
    }
}