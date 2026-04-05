package com.roomnexus.backend.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record RoomResponse(
        UUID id,
        String name,
        Integer capacity,
        String pictureUrl,
        String description,
        boolean available,
        UUID companyId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}