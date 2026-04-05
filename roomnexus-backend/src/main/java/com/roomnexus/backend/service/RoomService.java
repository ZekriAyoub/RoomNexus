package com.roomnexus.backend.service;

import com.roomnexus.backend.dto.CreateRoomRequest;
import com.roomnexus.backend.dto.RoomResponse;
import com.roomnexus.backend.dto.UpdateRoomRequest;
import com.roomnexus.backend.entity.Company;
import com.roomnexus.backend.entity.Room;
import com.roomnexus.backend.repository.CompanyRepository;
import com.roomnexus.backend.repository.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final CompanyRepository companyRepository;

    public RoomResponse createRoom(CreateRoomRequest request) {
        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Company not found with id: " + request.companyId()));

        Room room = new Room();
        room.setName(request.name());
        room.setCapacity(request.capacity());
        room.setPictureUrl(request.pictureUrl());
        room.setDescription(request.description());
        room.setAvailable(true);
        room.setCompany(company);

        return toResponse(roomRepository.save(room));
    }

    public List<RoomResponse> getRoomsByCompany(UUID companyId) {
        companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Company not found with id: " + companyId));

        return roomRepository.findByCompanyId(companyId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<RoomResponse> getAvailableRoomsByCompany(UUID companyId) {
        companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Company not found with id: " + companyId));

        return roomRepository.findByCompanyIdAndAvailable(companyId, true)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public RoomResponse getRoomById(UUID id) {
        return toResponse(findRoomById(id));
    }

    public RoomResponse updateRoom(UUID id, UpdateRoomRequest request) {
        Room room = findRoomById(id);

        room.setName(request.name());
        room.setCapacity(request.capacity());
        room.setPictureUrl(request.pictureUrl());
        room.setDescription(request.description());
        room.setAvailable(request.available());

        return toResponse(roomRepository.save(room));
    }

    public void deleteRoom(UUID id) {
        if (!roomRepository.existsById(id)) {
            throw new EntityNotFoundException("Room not found with id: " + id);
        }
        roomRepository.deleteById(id);
    }

    private Room findRoomById(UUID id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Room not found with id: " + id));
    }

    private RoomResponse toResponse(Room room) {
        return new RoomResponse(
                room.getId(),
                room.getName(),
                room.getCapacity(),
                room.getPictureUrl(),
                room.getDescription(),
                room.isAvailable(),
                room.getCompany().getId(),
                room.getCreatedAt(),
                room.getUpdatedAt()
        );
    }
}