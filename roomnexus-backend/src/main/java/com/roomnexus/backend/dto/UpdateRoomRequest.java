package com.roomnexus.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateRoomRequest(
        @NotBlank String name,
        @NotNull @Positive
        Integer capacity,
        String pictureUrl,
        @NotBlank
        String description,
        @NotNull
        Boolean available
) {}