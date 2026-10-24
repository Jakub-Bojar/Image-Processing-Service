package com.example.imageprocessingservice.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "image", indexes = @Index(name = "idx_image_owner_created_at", columnList = "owner_id, createdAt"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String originalFilename;

    @Column(nullable = false, unique = true)
    private String storageKey;

    @Column(nullable = false, length = 100)
    private String contentType;

    @Column(nullable = false)
    private long sizeBytes;

    @Column(nullable = false)
    private int width;

    @Column(nullable = false)
    private int height;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_image_id")
    private Image sourceImage;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
}
