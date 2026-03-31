CREATE TABLE company
(
    id                UUID PRIMARY KEY,
    name              VARCHAR(255) NOT NULL UNIQUE,
    keycloak_group_id VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE room
(
    id          UUID PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    capacity    INTEGER      NOT NULL,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP,
    picture_url VARCHAR(255),
    description TEXT         NOT NULL,
    available   BOOLEAN      NOT NULL DEFAULT TRUE,
    company_id  UUID         NOT NULL,

    CONSTRAINT fk_room_company
        FOREIGN KEY (company_id)
            REFERENCES company (id)
);

CREATE TABLE user_profile
(
    id               UUID PRIMARY KEY,
    first_name       VARCHAR(255) NOT NULL,
    last_name        VARCHAR(255) NOT NULL,
    keycloak_user_id VARCHAR(255) NOT NULL UNIQUE,
    company_id       UUID         NOT NULL,
    role             VARCHAR(50)  NOT NULL,
    created_at       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_user_profile_company
        FOREIGN KEY (company_id)
            REFERENCES company (id)
);

CREATE TABLE booking
(
    id         UUID PRIMARY KEY,
    start_time TIMESTAMP NOT NULL,
    end_time   TIMESTAMP NOT NULL,
    booked_by  UUID      NOT NULL,
    room_id    UUID      NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_booking_user_profile
        FOREIGN KEY (booked_by)
            REFERENCES user_profile (id),
    CONSTRAINT fk_booking_room
        FOREIGN KEY (room_id)
            REFERENCES room (id),

    CONSTRAINT chk_booking_times CHECK (end_time > start_time)
);