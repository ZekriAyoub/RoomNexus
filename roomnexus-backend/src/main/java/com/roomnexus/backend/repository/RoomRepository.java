package com.roomnexus.backend.repository;

import com.roomnexus.backend.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;
public interface RoomRepository extends JpaRepository<Room, UUID> {
    List<Room> findByCompanyId(UUID companyId);

    List<Room> findByCompanyIdAndAvailable(UUID companyId, boolean available);
}
