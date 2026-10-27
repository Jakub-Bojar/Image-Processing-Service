package com.example.imageprocessingservice.repository;

import com.example.imageprocessingservice.domain.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ImageRepository extends JpaRepository<Image, UUID> {
    Page<Image> findByOwnerId(Long ownerId, Pageable pageable);

    Optional<Image> findByIdAndOwnerId(UUID id, Long ownerId);
}