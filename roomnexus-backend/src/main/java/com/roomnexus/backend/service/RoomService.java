package com.roomnexus.backend.service;

import com.roomnexus.backend.dto.CreateRoomRequest;
import com.roomnexus.backend.dto.RoomResponse;
import com.roomnexus.backend.dto.UpdateRoomRequest;
import com.roomnexus.backend.entity.Company;
import com.roomnexus.backend.entity.Room;
import com.roomnexus.backend.entity.UserProfile;
import com.roomnexus.backend.exception.CompanyAccessGuard;
import com.roomnexus.backend.repository.CompanyRepository;
import com.roomnexus.backend.repository.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RoomService {

    private final RoomRepository roomRepository;
    private final CompanyRepository companyRepository;
    private final UserProfileService userProfileService;

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

        RoomResponse response = toResponse(roomRepository.save(room));
        log.info("Room created: id={} name={} company={}",
                response.id(),
                response.name(),
                response.companyId());
        return response;
    }

    @Transactional(readOnly = true)
    public List<RoomResponse> getRoomsByCompany(Jwt jwt, UUID companyId) {
        UserProfile user = userProfileService.getActiveUserProfile(jwt);
        CompanyAccessGuard.verify(user, companyId);

        companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Company not found with id: " + companyId));

        return roomRepository.findByCompanyId(companyId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RoomResponse> getAvailableRoomsByCompany(Jwt jwt, UUID companyId) {
        UserProfile user = userProfileService.getActiveUserProfile(jwt);
        CompanyAccessGuard.verify(user, companyId);

        companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Company not found with id: " + companyId));

        return roomRepository.findByCompanyIdAndAvailable(companyId, true)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public RoomResponse getRoomById(Jwt jwt, UUID id) {
        UserProfile user = userProfileService.getActiveUserProfile(jwt);
        Room room = findRoomById(id);
        CompanyAccessGuard.verify(user, room.getCompany().getId());
        return toResponse(room);
    }

    public RoomResponse updateRoom(Jwt jwt, UUID id, UpdateRoomRequest request) {
        UserProfile user = userProfileService.getActiveUserProfile(jwt);
        Room room = findRoomById(id);
        CompanyAccessGuard.verify(user, room.getCompany().getId());

        room.setName(request.name());
        room.setCapacity(request.capacity());
        room.setPictureUrl(request.pictureUrl());
        room.setDescription(request.description());
        room.setAvailable(request.available());

        RoomResponse response = toResponse(roomRepository.save(room));
        log.info("Room updated: id={} name={} capacity={} available={} description={} pictureUrl={}",
                response.id(),
                response.name(),
                response.capacity(),
                response.available(),
                response.description(),
                response.pictureUrl());
        return response;
    }


    public void deleteRoom(Jwt jwt, UUID id) {
        UserProfile user = userProfileService.getActiveUserProfile(jwt);
        Room room = findRoomById(id);
        CompanyAccessGuard.verify(user, room.getCompany().getId());

        roomRepository.delete(room);
        log.info("Room deleted: id={} name={} companyId={}",
                room.getId(),
                room.getName(),
                room.getCompany().getId());
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