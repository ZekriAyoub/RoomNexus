package com.roomnexus.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotBlank(message = "The company's name is required")
    @Column(unique = true, nullable = false)
    private String name;

    @NotBlank(message = "The company's keycloak group id is required")
    @Column(name = "keycloak_group_id", unique = true, nullable = false)
    private String keycloakGroupId;

}
