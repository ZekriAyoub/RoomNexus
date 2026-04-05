package com.roomnexus.backend.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record BookingResponse(
        UUID id,
        UUID roomId,
        String roomName,
        UUID bookedById,
        String bookedByFirstName,
        String bookedByLastName,
        LocalDateTime startTime,
        LocalDateTime endTime,
        LocalDateTime createdAt
) {}