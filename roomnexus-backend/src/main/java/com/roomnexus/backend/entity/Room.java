package com.roomnexus.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "room")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotBlank(message = "The room's name is required")
    private String name;

    @NotNull(message = "The capacity is required")
    @Positive(message = "The capacity must be Positive")
    private Integer capacity;

    private String pictureUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;
}
